package com.citygrid.smartlighting.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "journals")
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private JournalType type;   // PURCHASE, SALES, BANK

    public Journal() {}

    public Journal(String name, JournalType type) {
        this.name = name;
        this.type = type;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public JournalType getType() { return type; }
    public void setType(JournalType type) { this.type = type; }
}
