package com.citygrid.smartlighting.controller;

import com.citygrid.smartlighting.dto.PaymentRequestDTO;
import com.citygrid.smartlighting.entity.Payment;
import com.citygrid.smartlighting.entity.VendorBill;
import com.citygrid.smartlighting.service.PaymentService;
import com.citygrid.smartlighting.service.VendorBillService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor-bills")
public class VendorBillController {

    private final VendorBillService vendorBillService;
    private final PaymentService paymentService;

    public VendorBillController(VendorBillService vendorBillService, PaymentService paymentService) {
        this.vendorBillService = vendorBillService;
        this.paymentService = paymentService;
    }

    /** Step 1: PO -> draft Vendor Bill */
    @PostMapping("/from-purchase-order/{poId}")
    public ResponseEntity<VendorBill> createFromPO(@PathVariable Long poId) {
        return ResponseEntity.ok(vendorBillService.createFromPurchaseOrder(poId));
    }

    /** Step 2: post the bill into the ledger (Debit Expense/Asset, Credit Accounts Payable) */
    @PostMapping("/{id}/post")
    public ResponseEntity<VendorBill> post(@PathVariable Long id) {
        return ResponseEntity.ok(vendorBillService.postBill(id));
    }

    /** Step 3: pay the bill via bank (Debit Accounts Payable, Credit Bank) */
    @PostMapping("/{id}/pay")
    public ResponseEntity<Payment> pay(@PathVariable Long id, @Valid @RequestBody PaymentRequestDTO request) {
        return ResponseEntity.ok(paymentService.payVendorBill(id, request));
    }

    @GetMapping
    public ResponseEntity<List<VendorBill>> getAll() {
        return ResponseEntity.ok(vendorBillService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorBill> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vendorBillService.getById(id));
    }
}
