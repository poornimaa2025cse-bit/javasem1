package com.citygrid.smartlighting.controller;

import com.citygrid.smartlighting.dto.BalanceSheetDTO;
import com.citygrid.smartlighting.dto.ProfitAndLossDTO;
import com.citygrid.smartlighting.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/balance-sheet")
    public ResponseEntity<BalanceSheetDTO> getBalanceSheet() {
        return ResponseEntity.ok(reportService.getBalanceSheet());
    }

    @GetMapping("/profit-and-loss")
    public ResponseEntity<ProfitAndLossDTO> getProfitAndLoss() {
        return ResponseEntity.ok(reportService.getProfitAndLoss());
    }
}
