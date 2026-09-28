package com.citygrid.smartlighting.controller;

import com.citygrid.smartlighting.entity.AnalyticAccount;
import com.citygrid.smartlighting.service.AnalyticAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
public class AnalyticAccountController {

    private final AnalyticAccountService analyticAccountService;

    public AnalyticAccountController(AnalyticAccountService analyticAccountService) {
        this.analyticAccountService = analyticAccountService;
    }

    @PostMapping
    public ResponseEntity<AnalyticAccount> create(@RequestBody AnalyticAccount zone) {
        return ResponseEntity.ok(analyticAccountService.create(zone));
    }

    @GetMapping
    public ResponseEntity<List<AnalyticAccount>> getAll() {
        return ResponseEntity.ok(analyticAccountService.getAll());
    }
}
