package com.citygrid.smartlighting.controller;

import com.citygrid.smartlighting.dto.DimmingRequestDTO;
import com.citygrid.smartlighting.dto.PowerSummaryDTO;
import com.citygrid.smartlighting.dto.TelemetryRequestDTO;
import com.citygrid.smartlighting.entity.StreetLight;
import com.citygrid.smartlighting.service.StreetLightService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/street-lights")
public class StreetLightController {

    private final StreetLightService streetLightService;

    public StreetLightController(StreetLightService streetLightService) {
        this.streetLightService = streetLightService;
    }

    @PostMapping
    public ResponseEntity<StreetLight> create(@RequestBody StreetLight light) {
        return ResponseEntity.ok(streetLightService.create(light));
    }

    @GetMapping
    public ResponseEntity<List<StreetLight>> getAll() {
        return ResponseEntity.ok(streetLightService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StreetLight> getById(@PathVariable Long id) {
        return ResponseEntity.ok(streetLightService.getById(id));
    }

    /** Adjust dimming - system or admin action. May auto-raise a fault ticket. */
    @PutMapping("/{id}/dimming")
    public ResponseEntity<StreetLight> setDimming(@PathVariable Long id, @Valid @RequestBody DimmingRequestDTO request) {
        return ResponseEntity.ok(streetLightService.updateDimming(id, request));
    }

    /** A pole controller pushes a live sensor reading here. */
    @PostMapping("/{id}/telemetry")
    public ResponseEntity<StreetLight> pushTelemetry(@PathVariable Long id, @Valid @RequestBody TelemetryRequestDTO request) {
        return ResponseEntity.ok(streetLightService.ingestTelemetry(id, request));
    }

    /** GET /api/street-lights/power-summary - must be declared before /{id} would clash,
     *  but since this is a literal path segment "power-summary" (not numeric), Spring
     *  matches it correctly as long as it's mapped explicitly like this. */
    @GetMapping("/power-summary")
    public ResponseEntity<List<PowerSummaryDTO>> getPowerSummary() {
        return ResponseEntity.ok(streetLightService.getPowerSummary());
    }
}
