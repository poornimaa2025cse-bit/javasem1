package com.citygrid.smartlighting.repository;

import com.citygrid.smartlighting.entity.FaultTicket;
import com.citygrid.smartlighting.entity.StreetLight;
import com.citygrid.smartlighting.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FaultTicketRepository extends JpaRepository<FaultTicket, Long> {
    List<FaultTicket> findByStatus(TicketStatus status);
    List<FaultTicket> findByStreetLightAndStatus(StreetLight streetLight, TicketStatus status);
}
