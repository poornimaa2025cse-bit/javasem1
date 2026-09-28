package com.citygrid.smartlighting.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * A single sensor reading pushed from a pole's controller.
 * Kept as a historical log even after StreetLight's "current" fields are updated.
 */
@Entity
@Table(name = "telemetry_readings")
public class TelemetryReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "street_light_id")
    private StreetLight streetLight;

    private Double powerDrawWatts;
    private Double ambientLightLux;
    private Boolean nightActive;
    private LocalDateTime recordedAt;

    public TelemetryReading() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StreetLight getStreetLight() { return streetLight; }
    public void setStreetLight(StreetLight streetLight) { this.streetLight = streetLight; }

    public Double getPowerDrawWatts() { return powerDrawWatts; }
    public void setPowerDrawWatts(Double powerDrawWatts) { this.powerDrawWatts = powerDrawWatts; }

    public Double getAmbientLightLux() { return ambientLightLux; }
    public void setAmbientLightLux(Double ambientLightLux) { this.ambientLightLux = ambientLightLux; }

    public Boolean getNightActive() { return nightActive; }
    public void setNightActive(Boolean nightActive) { this.nightActive = nightActive; }

    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}
