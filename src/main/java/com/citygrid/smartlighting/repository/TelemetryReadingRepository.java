package com.citygrid.smartlighting.repository;

import com.citygrid.smartlighting.entity.StreetLight;
import com.citygrid.smartlighting.entity.TelemetryReading;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TelemetryReadingRepository extends JpaRepository<TelemetryReading, Long> {
    List<TelemetryReading> findByStreetLightOrderByRecordedAtDesc(StreetLight streetLight);
}
