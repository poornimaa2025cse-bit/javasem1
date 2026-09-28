package com.citygrid.smartlighting.service;

import com.citygrid.smartlighting.entity.FaultTicket;
import com.citygrid.smartlighting.entity.TicketStatus;
import com.citygrid.smartlighting.exception.ResourceNotFoundException;
import com.citygrid.smartlighting.repository.FaultTicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaultTicketService {

    private final FaultTicketRepository faultTicketRepository;

    public FaultTicketService(FaultTicketRepository faultTicketRepository) {
        this.faultTicketRepository = faultTicketRepository;
    }

    public List<FaultTicket> getAll() {
        return faultTicketRepository.findAll();
    }

    public List<FaultTicket> getOpen() {
        return faultTicketRepository.findByStatus(TicketStatus.OPEN);
    }

    public FaultTicket resolve(Long id) {
        FaultTicket ticket = faultTicketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fault ticket not found: " + id));
        ticket.setStatus(TicketStatus.RESOLVED);
        return faultTicketRepository.save(ticket);
    }
}
