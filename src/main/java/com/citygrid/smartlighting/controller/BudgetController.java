package com.citygrid.smartlighting.controller;

import com.citygrid.smartlighting.dto.BudgetVarianceDTO;
import com.citygrid.smartlighting.entity.Budget;
import com.citygrid.smartlighting.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<Budget> create(@RequestBody Budget budget) {
        return ResponseEntity.ok(budgetService.create(budget));
    }

    @GetMapping
    public ResponseEntity<List<Budget>> getAll() {
        return ResponseEntity.ok(budgetService.getAll());
    }

    @GetMapping("/variance-report")
    public ResponseEntity<List<BudgetVarianceDTO>> getVarianceReport() {
        return ResponseEntity.ok(budgetService.getVarianceReport());
    }
}
