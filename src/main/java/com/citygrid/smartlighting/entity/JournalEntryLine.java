package com.citygrid.smartlighting.entity;

import jakarta.persistence.*;

/**
 * One debit or credit line inside a JournalEntry.
 * A single line normally has either a debit OR a credit amount (the other is 0).
 */
@Entity
@Table(name = "journal_entry_lines")
public class JournalEntryLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private Double debit = 0.0;
    private Double credit = 0.0;

    @ManyToOne
    @JoinColumn(name = "journal_entry_id")
    private JournalEntry journalEntry;

    public JournalEntryLine() {}

    public JournalEntryLine(Account account, Double debit, Double credit) {
        this.account = account;
        this.debit = debit;
        this.credit = credit;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public Double getDebit() { return debit; }
    public void setDebit(Double debit) { this.debit = debit; }

    public Double getCredit() { return credit; }
    public void setCredit(Double credit) { this.credit = credit; }

    public JournalEntry getJournalEntry() { return journalEntry; }
    public void setJournalEntry(JournalEntry journalEntry) { this.journalEntry = journalEntry; }
}
