package com.citygrid.smartlighting.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Auto-generated (or manually raised) maintenance ticket for a pole.
 * The system creates one automatically when a pole reports zero power
 * draw while it should be lit (nightActive = true).
 */
@Entity
@Table(name = "fault_tickets")
public class FaultTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "street_light_id")
    private StreetLight streetLight;

    private String description;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;   // OPEN, IN_PROGRESS, RESOLVED

    private LocalDateTime createdAt;

    public FaultTicket() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StreetLight getStreetLight() { return streetLight; }
    public void setStreetLight(StreetLight streetLight) { this.streetLight = streetLight; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TicketStatus getStatus() { return status; }
    public void setStatus(TicketStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
