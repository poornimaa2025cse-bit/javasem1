package com.citygrid.smartlighting.controller;

import com.citygrid.smartlighting.entity.FaultTicket;
import com.citygrid.smartlighting.service.FaultTicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fault-tickets")
public class FaultTicketController {

    private final FaultTicketService faultTicketService;

    public FaultTicketController(FaultTicketService faultTicketService) {
        this.faultTicketService = faultTicketService;
    }

    @GetMapping
    public ResponseEntity<List<FaultTicket>> getAll() {
        return ResponseEntity.ok(faultTicketService.getAll());
    }

    @GetMapping("/open")
    public ResponseEntity<List<FaultTicket>> getOpen() {
        return ResponseEntity.ok(faultTicketService.getOpen());
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<FaultTicket> resolve(@PathVariable Long id) {
        return ResponseEntity.ok(faultTicketService.resolve(id));
    }
}
