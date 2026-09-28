package com.citygrid.smartlighting.service;

import com.citygrid.smartlighting.dto.BudgetVarianceDTO;
import com.citygrid.smartlighting.entity.Budget;
import com.citygrid.smartlighting.entity.BillStatus;
import com.citygrid.smartlighting.entity.VendorBill;
import com.citygrid.smartlighting.repository.BudgetRepository;
import com.citygrid.smartlighting.repository.VendorBillRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Compares planned zone budgets against actual posted spend (electricity + repair bills).
 * "Actual" is always computed live from VendorBill records, so the variance report
 * never goes stale even if new bills are posted after the Budget row was created.
 */
@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final VendorBillRepository vendorBillRepository;

    public BudgetService(BudgetRepository budgetRepository, VendorBillRepository vendorBillRepository) {
        this.budgetRepository = budgetRepository;
        this.vendorBillRepository = vendorBillRepository;
    }

    public Budget create(Budget budget) {
        return budgetRepository.save(budget);
    }

    public List<Budget> getAll() {
        return budgetRepository.findAll();
    }

    /** Zone energy consumption budget variance report (Reporting Requirement #3). */
    public List<BudgetVarianceDTO> getVarianceReport() {
        return budgetRepository.findAll().stream().map(budget -> {
            YearMonth ym = YearMonth.parse(budget.getPeriod()); // expects "yyyy-MM"
            LocalDate start = ym.atDay(1);
            LocalDate end = ym.atEndOfMonth();

            List<VendorBill> billsInPeriod = new ArrayList<>(vendorBillRepository
                    .findByAnalyticAccountAndStatusAndBillDateBetween(
                            budget.getAnalyticAccount(), BillStatus.POSTED, start, end));
            billsInPeriod.addAll(vendorBillRepository
                    .findByAnalyticAccountAndStatusAndBillDateBetween(
                            budget.getAnalyticAccount(), BillStatus.PAID, start, end));

            double actual = billsInPeriod.stream()
                    .mapToDouble(b -> b.getAmount() == null ? 0.0 : b.getAmount())
                    .sum();

            return new BudgetVarianceDTO(
                    budget.getAnalyticAccount().getZoneName(),
                    budget.getPeriod(),
                    budget.getPlannedAmount(),
                    actual
            );
        }).collect(Collectors.toList());
    }
}
