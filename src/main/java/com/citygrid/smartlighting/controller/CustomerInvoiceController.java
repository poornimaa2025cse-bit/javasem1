package com.citygrid.smartlighting.controller;

import com.citygrid.smartlighting.dto.PaymentRequestDTO;
import com.citygrid.smartlighting.entity.CustomerInvoice;
import com.citygrid.smartlighting.entity.Payment;
import com.citygrid.smartlighting.service.CustomerInvoiceService;
import com.citygrid.smartlighting.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-invoices")
public class CustomerInvoiceController {

    private final CustomerInvoiceService customerInvoiceService;
    private final PaymentService paymentService;

    public CustomerInvoiceController(CustomerInvoiceService customerInvoiceService, PaymentService paymentService) {
        this.customerInvoiceService = customerInvoiceService;
        this.paymentService = paymentService;
    }

    /** Step 1: SO -> draft Customer Invoice */
    @PostMapping("/from-sales-order/{soId}")
    public ResponseEntity<CustomerInvoice> createFromSO(@PathVariable Long soId) {
        return ResponseEntity.ok(customerInvoiceService.createFromSalesOrder(soId));
    }

    /** Step 2: post the invoice into the ledger (Debit Receivable, Credit Income) */
    @PostMapping("/{id}/post")
    public ResponseEntity<CustomerInvoice> post(@PathVariable Long id) {
        return ResponseEntity.ok(customerInvoiceService.postInvoice(id));
    }

    /** Step 3: register the municipality's payment receipt via bank */
    @PostMapping("/{id}/receive-payment")
    public ResponseEntity<Payment> receivePayment(@PathVariable Long id, @Valid @RequestBody PaymentRequestDTO request) {
        return ResponseEntity.ok(paymentService.receiveCustomerPayment(id, request));
    }

    @GetMapping
    public ResponseEntity<List<CustomerInvoice>> getAll() {
        return ResponseEntity.ok(customerInvoiceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerInvoice> getById(@PathVariable Long id) {
        return ResponseEntity.ok(customerInvoiceService.getById(id));
    }
}
