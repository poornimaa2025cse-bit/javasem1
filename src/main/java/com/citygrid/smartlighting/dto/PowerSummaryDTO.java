package com.citygrid.smartlighting.dto;

public class PowerSummaryDTO {

    private String zoneName;
    private long lightCount;
    private double totalPowerWatts;
    private double averageDimmingPercent;
    private long openFaultTickets;

    public PowerSummaryDTO() {}

    public PowerSummaryDTO(String zoneName, long lightCount, double totalPowerWatts,
                            double averageDimmingPercent, long openFaultTickets) {
        this.zoneName = zoneName;
        this.lightCount = lightCount;
        this.totalPowerWatts = totalPowerWatts;
        this.averageDimmingPercent = averageDimmingPercent;
        this.openFaultTickets = openFaultTickets;
    }

    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }

    public long getLightCount() { return lightCount; }
    public void setLightCount(long lightCount) { this.lightCount = lightCount; }

    public double getTotalPowerWatts() { return totalPowerWatts; }
    public void setTotalPowerWatts(double totalPowerWatts) { this.totalPowerWatts = totalPowerWatts; }

    public double getAverageDimmingPercent() { return averageDimmingPercent; }
    public void setAverageDimmingPercent(double averageDimmingPercent) { this.averageDimmingPercent = averageDimmingPercent; }

    public long getOpenFaultTickets() { return openFaultTickets; }
    public void setOpenFaultTickets(long openFaultTickets) { this.openFaultTickets = openFaultTickets; }
}
