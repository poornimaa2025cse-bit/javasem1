package com.citygrid.smartlighting.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DimmingRequestDTO {

    @NotNull
    @Min(0)
    @Max(100)
    private Integer dimmingPercent;

    public Integer getDimmingPercent() { return dimmingPercent; }
    public void setDimmingPercent(Integer dimmingPercent) { this.dimmingPercent = dimmingPercent; }
}
