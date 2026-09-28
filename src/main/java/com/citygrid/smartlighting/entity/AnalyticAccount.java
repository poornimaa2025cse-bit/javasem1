package com.citygrid.smartlighting.entity;

import jakarta.persistence.*;

/**
 * A cost-center / zone tag (e.g. "Highway Zone 7 Smart Grid").
 * Attached to expenses so costs can be reported per zone, separate
 * from the main Chart of Accounts.
 */
@Entity
@Table(name = "analytic_accounts")
public class AnalyticAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String zoneName;

    public AnalyticAccount() {}

    public AnalyticAccount(String zoneName) {
        this.zoneName = zoneName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }
}
