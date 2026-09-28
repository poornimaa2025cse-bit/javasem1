package com.citygrid.smartlighting.service;

import com.citygrid.smartlighting.entity.*;
import com.citygrid.smartlighting.repository.JournalEntryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * The heart of the double-entry bookkeeping engine.
 * Every accounting transaction in the app (bill posting, payment, invoice, receipt)
 * ultimately calls post() here with exactly one debit line and one credit line
 * (or more, for split transactions), and this service guarantees the entry balances
 * before it is ever saved.
 */
@Service
public class JournalPostingService {

    private final JournalEntryRepository journalEntryRepository;

    public JournalPostingService(JournalEntryRepository journalEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
    }

    public JournalEntry post(Journal journal, String reference, String description,
                              LocalDate date, Account debitAccount, Account creditAccount, Double amount) {

        JournalEntry entry = new JournalEntry();
        entry.setJournal(journal);
        entry.setReference(reference);
        entry.setDescription(description);
        entry.setEntryDate(date == null ? LocalDate.now() : date);

        entry.addLine(new JournalEntryLine(debitAccount, amount, 0.0));
        entry.addLine(new JournalEntryLine(creditAccount, 0.0, amount));

        if (!entry.isBalanced()) {
            // This should never happen with a simple two-line entry, but the check
            // is kept here because it is the fundamental rule of double-entry accounting.
            throw new IllegalStateException("Journal entry is not balanced for reference: " + reference);
        }

        return journalEntryRepository.save(entry);
    }
}
