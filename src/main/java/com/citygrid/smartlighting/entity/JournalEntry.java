package com.citygrid.smartlighting.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The header of a double-entry bookkeeping record.
 * Every JournalEntry must have lines whose total debit == total credit.
 */
@Entity
@Table(name = "journal_entries")
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate entryDate;
    private String reference;   // e.g. "BILL-12", "INV-7", "PAY-3"
    private String description;

    @ManyToOne
    @JoinColumn(name = "journal_id")
    private Journal journal;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JournalEntryLine> lines = new ArrayList<>();

    public JournalEntry() {}

    public void addLine(JournalEntryLine line) {
        line.setJournalEntry(this);
        this.lines.add(line);
    }

    /** Core accounting rule: total debits must equal total credits. */
    public boolean isBalanced() {
        double totalDebit = 0.0;
        double totalCredit = 0.0;
        for (JournalEntryLine line : lines) {
            totalDebit += line.getDebit() == null ? 0.0 : line.getDebit();
            totalCredit += line.getCredit() == null ? 0.0 : line.getCredit();
        }
        return Math.abs(totalDebit - totalCredit) < 0.01;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Journal getJournal() { return journal; }
    public void setJournal(Journal journal) { this.journal = journal; }

    public List<JournalEntryLine> getLines() { return lines; }
    public void setLines(List<JournalEntryLine> lines) { this.lines = lines; }
}
