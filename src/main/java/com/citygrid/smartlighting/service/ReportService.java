package com.citygrid.smartlighting.service;

import com.citygrid.smartlighting.dto.BalanceSheetDTO;
import com.citygrid.smartlighting.dto.ProfitAndLossDTO;
import com.citygrid.smartlighting.entity.Account;
import com.citygrid.smartlighting.entity.AccountType;
import com.citygrid.smartlighting.entity.JournalEntryLine;
import com.citygrid.smartlighting.repository.AccountRepository;
import com.citygrid.smartlighting.repository.JournalEntryLineRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates the two financial statements straight from posted journal entry
 * lines - no separate "ledger balance" table is kept, so the reports are
 * always in sync with whatever has actually been posted.
 *
 * Normal balance convention used here:
 *   ASSET / EXPENSE   -> balance = total debit - total credit
 *   LIABILITY / INCOME -> balance = total credit - total debit
 */
@Service
public class ReportService {

    private final AccountRepository accountRepository;
    private final JournalEntryLineRepository journalEntryLineRepository;

    public ReportService(AccountRepository accountRepository,
                          JournalEntryLineRepository journalEntryLineRepository) {
        this.accountRepository = accountRepository;
        this.journalEntryLineRepository = journalEntryLineRepository;
    }

    private double netBalance(Account account, boolean debitNormal) {
        List<JournalEntryLine> lines = journalEntryLineRepository.findByAccount(account);
        double totalDebit = lines.stream().mapToDouble(l -> l.getDebit() == null ? 0.0 : l.getDebit()).sum();
        double totalCredit = lines.stream().mapToDouble(l -> l.getCredit() == null ? 0.0 : l.getCredit()).sum();
        return debitNormal ? (totalDebit - totalCredit) : (totalCredit - totalDebit);
    }

    public BalanceSheetDTO getBalanceSheet() {
        Map<String, Double> assets = new LinkedHashMap<>();
        Map<String, Double> liabilities = new LinkedHashMap<>();

        for (Account account : accountRepository.findByType(AccountType.ASSET)) {
            assets.put(account.getName(), netBalance(account, true));
        }
        for (Account account : accountRepository.findByType(AccountType.LIABILITY)) {
            liabilities.put(account.getName(), netBalance(account, false));
        }

        double totalAssets = assets.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalLiabilities = liabilities.values().stream().mapToDouble(Double::doubleValue).sum();

        return new BalanceSheetDTO(assets, liabilities, totalAssets, totalLiabilities);
    }

    public ProfitAndLossDTO getProfitAndLoss() {
        Map<String, Double> income = new LinkedHashMap<>();
        Map<String, Double> expenses = new LinkedHashMap<>();

        for (Account account : accountRepository.findByType(AccountType.INCOME)) {
            income.put(account.getName(), netBalance(account, false));
        }
        for (Account account : accountRepository.findByType(AccountType.EXPENSE)) {
            expenses.put(account.getName(), netBalance(account, true));
        }

        double totalIncome = income.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalExpenses = expenses.values().stream().mapToDouble(Double::doubleValue).sum();

        return new ProfitAndLossDTO(income, expenses, totalIncome, totalExpenses);
    }
}
