package com.citygrid.smartlighting.repository;

import com.citygrid.smartlighting.entity.AnalyticAccount;
import com.citygrid.smartlighting.entity.StreetLight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StreetLightRepository extends JpaRepository<StreetLight, Long> {
    List<StreetLight> findByAnalyticAccount(AnalyticAccount analyticAccount);
}
