package com.citygrid.smartlighting.dto;

import java.util.Map;

public class ProfitAndLossDTO {

    private Map<String, Double> income;
    private Map<String, Double> expenses;
    private double totalIncome;
    private double totalExpenses;
    private double netProfit;

    public ProfitAndLossDTO() {}

    public ProfitAndLossDTO(Map<String, Double> income, Map<String, Double> expenses,
                             double totalIncome, double totalExpenses) {
        this.income = income;
        this.expenses = expenses;
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.netProfit = totalIncome - totalExpenses;
    }

    public Map<String, Double> getIncome() { return income; }
    public void setIncome(Map<String, Double> income) { this.income = income; }

    public Map<String, Double> getExpenses() { return expenses; }
    public void setExpenses(Map<String, Double> expenses) { this.expenses = expenses; }

    public double getTotalIncome() { return totalIncome; }
    public void setTotalIncome(double totalIncome) { this.totalIncome = totalIncome; }

    public double getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(double totalExpenses) { this.totalExpenses = totalExpenses; }

    public double getNetProfit() { return netProfit; }
    public void setNetProfit(double netProfit) { this.netProfit = netProfit; }
}
