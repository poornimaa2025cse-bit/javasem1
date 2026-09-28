package com.citygrid.smartlighting.entity;

import jakarta.persistence.*;

/**
 * Planned spend for one zone (AnalyticAccount) over one period.
 * Actual spend is NOT stored here - it is calculated on demand
 * from posted Vendor Bills in BudgetService, so it's always up to date.
 */
@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "analytic_account_id")
    private AnalyticAccount analyticAccount;

    private String period;       // e.g. "2026-09" (year-month)
    private Double plannedAmount;

    public Budget() {}

    public Budget(AnalyticAccount analyticAccount, String period, Double plannedAmount) {
        this.analyticAccount = analyticAccount;
        this.period = period;
        this.plannedAmount = plannedAmount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AnalyticAccount getAnalyticAccount() { return analyticAccount; }
    public void setAnalyticAccount(AnalyticAccount analyticAccount) { this.analyticAccount = analyticAccount; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public Double getPlannedAmount() { return plannedAmount; }
    public void setPlannedAmount(Double plannedAmount) { this.plannedAmount = plannedAmount; }
}
