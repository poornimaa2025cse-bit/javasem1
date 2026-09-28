package com.citygrid.smartlighting.controller;

import com.citygrid.smartlighting.entity.SalesOrder;
import com.citygrid.smartlighting.service.SalesOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-orders")
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    public SalesOrderController(SalesOrderService salesOrderService) {
        this.salesOrderService = salesOrderService;
    }

    @PostMapping
    public ResponseEntity<SalesOrder> create(@RequestBody SalesOrder so) {
        return ResponseEntity.ok(salesOrderService.create(so));
    }

    @GetMapping
    public ResponseEntity<List<SalesOrder>> getAll() {
        return ResponseEntity.ok(salesOrderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrder> getById(@PathVariable Long id) {
        return ResponseEntity.ok(salesOrderService.getById(id));
    }
}
