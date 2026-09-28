package com.citygrid.smartlighting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Smart Street Light Controller & Municipal Energy ERP.
 *
 * Run with:  mvn spring-boot:run
 * Then visit: http://localhost:8080/api/street-lights
 * H2 console: http://localhost:8080/h2-console  (JDBC URL: jdbc:h2:mem:smartlightingdb)
 */
@SpringBootApplication
public class SmartLightingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartLightingApplication.class, args);
    }
}
