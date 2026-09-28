package com.citygrid.smartlighting.service;

import com.citygrid.smartlighting.dto.DimmingRequestDTO;
import com.citygrid.smartlighting.dto.PowerSummaryDTO;
import com.citygrid.smartlighting.dto.TelemetryRequestDTO;
import com.citygrid.smartlighting.entity.*;
import com.citygrid.smartlighting.exception.ResourceNotFoundException;
import com.citygrid.smartlighting.repository.FaultTicketRepository;
import com.citygrid.smartlighting.repository.StreetLightRepository;
import com.citygrid.smartlighting.repository.TelemetryReadingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Owns the pole hardware model: dimming control, telemetry ingestion, and the
 * automated fault-detection rule described in the spec:
 *   "if power draw is zero during night status -> auto-generate a fault ticket".
 */
@Service
public class StreetLightService {

    private final StreetLightRepository streetLightRepository;
    private final TelemetryReadingRepository telemetryReadingRepository;
    private final FaultTicketRepository faultTicketRepository;

    public StreetLightService(StreetLightRepository streetLightRepository,
                               TelemetryReadingRepository telemetryReadingRepository,
                               FaultTicketRepository faultTicketRepository) {
        this.streetLightRepository = streetLightRepository;
        this.telemetryReadingRepository = telemetryReadingRepository;
        this.faultTicketRepository = faultTicketRepository;
    }

    public StreetLight create(StreetLight light) {
        return streetLightRepository.save(light);
    }

    public List<StreetLight> getAll() {
        return streetLightRepository.findAll();
    }

    public StreetLight getById(Long id) {
        return streetLightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Street light not found: " + id));
    }

    /** PUT /api/street-lights/{id}/dimming */
    public StreetLight updateDimming(Long id, DimmingRequestDTO request) {
        StreetLight light = getById(id);
        light.setDimmingPercent(request.getDimmingPercent());
        streetLightRepository.save(light);
        checkForFault(light);
        return light;
    }

    /** Ingest a live sensor reading from a pole controller. */
    public StreetLight ingestTelemetry(Long id, TelemetryRequestDTO request) {
        StreetLight light = getById(id);
        light.setPowerDrawWatts(request.getPowerDrawWatts());
        light.setAmbientLightLux(request.getAmbientLightLux());
        light.setNightActive(request.getNightActive());
        streetLightRepository.save(light);

        TelemetryReading reading = new TelemetryReading();
        reading.setStreetLight(light);
        reading.setPowerDrawWatts(request.getPowerDrawWatts());
        reading.setAmbientLightLux(request.getAmbientLightLux());
        reading.setNightActive(request.getNightActive());
        reading.setRecordedAt(LocalDateTime.now());
        telemetryReadingRepository.save(reading);

        checkForFault(light);
        return light;
    }

    /**
     * Core business rule: if the pole should be lit (nightActive = true) but is
     * drawing zero power, it is very likely a bulb/wiring fault - raise a ticket
     * automatically, unless one is already open for this pole.
     */
    private void checkForFault(StreetLight light) {
        boolean isZeroPower = light.getPowerDrawWatts() != null && light.getPowerDrawWatts() == 0.0;
        boolean shouldBeLit = Boolean.TRUE.equals(light.getNightActive());

        if (isZeroPower && shouldBeLit) {
            List<FaultTicket> existingOpen = faultTicketRepository.findByStreetLightAndStatus(light, TicketStatus.OPEN);
            if (existingOpen.isEmpty()) {
                FaultTicket ticket = new FaultTicket();
                ticket.setStreetLight(light);
                ticket.setDescription("Zero power draw detected on pole " + light.getPoleCode() +
                        " during active night hours. Possible bulb or wiring fault.");
                ticket.setStatus(TicketStatus.OPEN);
                ticket.setCreatedAt(LocalDateTime.now());
                faultTicketRepository.save(ticket);
            }
        }
    }

    /** GET /api/street-lights/power-summary - rollup grouped by zone. */
    public List<PowerSummaryDTO> getPowerSummary() {
        List<StreetLight> all = streetLightRepository.findAll();

        Map<String, List<StreetLight>> byZone = all.stream()
                .collect(Collectors.groupingBy(l ->
                        l.getAnalyticAccount() == null ? "Unassigned" : l.getAnalyticAccount().getZoneName()));

        return byZone.entrySet().stream().map(entry -> {
            String zoneName = entry.getKey();
            List<StreetLight> lights = entry.getValue();

            double totalPower = lights.stream()
                    .mapToDouble(l -> l.getPowerDrawWatts() == null ? 0.0 : l.getPowerDrawWatts())
                    .sum();
            double avgDimming = lights.stream()
                    .mapToInt(l -> l.getDimmingPercent() == null ? 0 : l.getDimmingPercent())
                    .average().orElse(0.0);
            long openTickets = lights.stream()
                    .flatMap(l -> faultTicketRepository.findByStreetLightAndStatus(l, TicketStatus.OPEN).stream())
                    .count();

            return new PowerSummaryDTO(zoneName, lights.size(), totalPower, avgDimming, openTickets);
        }).collect(Collectors.toList());
    }
}
