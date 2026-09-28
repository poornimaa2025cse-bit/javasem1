package com.citygrid.smartlighting.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "street_lights")
public class StreetLight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String poleCode;

    @ManyToOne
    @JoinColumn(name = "analytic_account_id")
    private AnalyticAccount analyticAccount;   // the zone this pole belongs to

    private Integer dimmingPercent = 100;      // 0-100
    private Double powerDrawWatts = 0.0;
    private Double ambientLightLux = 0.0;
    private Boolean nightActive = false;       // true when pole should be lit (dusk-to-dawn)

    public StreetLight() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPoleCode() { return poleCode; }
    public void setPoleCode(String poleCode) { this.poleCode = poleCode; }

    public AnalyticAccount getAnalyticAccount() { return analyticAccount; }
    public void setAnalyticAccount(AnalyticAccount analyticAccount) { this.analyticAccount = analyticAccount; }

    public Integer getDimmingPercent() { return dimmingPercent; }
    public void setDimmingPercent(Integer dimmingPercent) { this.dimmingPercent = dimmingPercent; }

    public Double getPowerDrawWatts() { return powerDrawWatts; }
    public void setPowerDrawWatts(Double powerDrawWatts) { this.powerDrawWatts = powerDrawWatts; }

    public Double getAmbientLightLux() { return ambientLightLux; }
    public void setAmbientLightLux(Double ambientLightLux) { this.ambientLightLux = ambientLightLux; }

    public Boolean getNightActive() { return nightActive; }
    public void setNightActive(Boolean nightActive) { this.nightActive = nightActive; }
}
