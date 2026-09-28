package com.citygrid.smartlighting.dto;

import java.util.Map;

public class BalanceSheetDTO {

    private Map<String, Double> assets;
    private Map<String, Double> liabilities;
    private double totalAssets;
    private double totalLiabilities;

    public BalanceSheetDTO() {}

    public BalanceSheetDTO(Map<String, Double> assets, Map<String, Double> liabilities,
                            double totalAssets, double totalLiabilities) {
        this.assets = assets;
        this.liabilities = liabilities;
        this.totalAssets = totalAssets;
        this.totalLiabilities = totalLiabilities;
    }

    public Map<String, Double> getAssets() { return assets; }
    public void setAssets(Map<String, Double> assets) { this.assets = assets; }

    public Map<String, Double> getLiabilities() { return liabilities; }
    public void setLiabilities(Map<String, Double> liabilities) { this.liabilities = liabilities; }

    public double getTotalAssets() { return totalAssets; }
    public void setTotalAssets(double totalAssets) { this.totalAssets = totalAssets; }

    public double getTotalLiabilities() { return totalLiabilities; }
    public void setTotalLiabilities(double totalLiabilities) { this.totalLiabilities = totalLiabilities; }
}
