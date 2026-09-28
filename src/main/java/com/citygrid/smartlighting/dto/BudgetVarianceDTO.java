package com.citygrid.smartlighting.dto;

public class BudgetVarianceDTO {

    private String zoneName;
    private String period;
    private double plannedAmount;
    private double actualAmount;
    private double variance;   // plannedAmount - actualAmount (negative = over budget)

    public BudgetVarianceDTO() {}

    public BudgetVarianceDTO(String zoneName, String period, double plannedAmount, double actualAmount) {
        this.zoneName = zoneName;
        this.period = period;
        this.plannedAmount = plannedAmount;
        this.actualAmount = actualAmount;
        this.variance = plannedAmount - actualAmount;
    }

    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public double getPlannedAmount() { return plannedAmount; }
    public void setPlannedAmount(double plannedAmount) { this.plannedAmount = plannedAmount; }

    public double getActualAmount() { return actualAmount; }
    public void setActualAmount(double actualAmount) { this.actualAmount = actualAmount; }

    public double getVariance() { return variance; }
    public void setVariance(double variance) { this.variance = variance; }
}
