package com.citygrid.smartlighting.dto;

import jakarta.validation.constraints.NotNull;

public class TelemetryRequestDTO {

    @NotNull
    private Double powerDrawWatts;

    private Double ambientLightLux;

    @NotNull
    private Boolean nightActive;

    public Double getPowerDrawWatts() { return powerDrawWatts; }
    public void setPowerDrawWatts(Double powerDrawWatts) { this.powerDrawWatts = powerDrawWatts; }

    public Double getAmbientLightLux() { return ambientLightLux; }
    public void setAmbientLightLux(Double ambientLightLux) { this.ambientLightLux = ambientLightLux; }

    public Boolean getNightActive() { return nightActive; }
    public void setNightActive(Boolean nightActive) { this.nightActive = nightActive; }
}
