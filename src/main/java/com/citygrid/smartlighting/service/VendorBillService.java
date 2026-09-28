package com.citygrid.smartlighting.service;

import com.citygrid.smartlighting.config.AccountCodes;
import com.citygrid.smartlighting.entity.*;
import com.citygrid.smartlighting.exception.InvalidOperationException;
import com.citygrid.smartlighting.exception.ResourceNotFoundException;
import com.citygrid.smartlighting.repository.JournalRepository;
import com.citygrid.smartlighting.repository.VendorBillRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Handles the "Vendor Bill" step of the Procure-to-Pay cycle:
 *   Purchase Order  ->  Vendor Bill (this class)  ->  Payment (see PaymentService)
 */
@Service
public class VendorBillService {

    private final VendorBillRepository vendorBillRepository;
    private final PurchaseOrderService purchaseOrderService;
    private final JournalRepository journalRepository;
    private final JournalPostingService journalPostingService;
    private final AccountService accountService;

    public VendorBillService(VendorBillRepository vendorBillRepository,
                              PurchaseOrderService purchaseOrderService,
                              JournalRepository journalRepository,
                              JournalPostingService journalPostingService,
                              AccountService accountService) {
        this.vendorBillRepository = vendorBillRepository;
        this.purchaseOrderService = purchaseOrderService;
        this.journalRepository = journalRepository;
        this.journalPostingService = journalPostingService;
        this.accountService = accountService;
    }

    /** Step 1: convert a confirmed Purchase Order into a draft Vendor Bill. */
    public VendorBill createFromPurchaseOrder(Long purchaseOrderId) {
        PurchaseOrder po = purchaseOrderService.getById(purchaseOrderId);

        VendorBill bill = new VendorBill();
        bill.setPurchaseOrder(po);
        bill.setVendor(po.getVendor());
        bill.setAnalyticAccount(po.getAnalyticAccount());
        bill.setAmount(po.getAmount());
        bill.setBillDate(LocalDate.now());
        bill.setStatus(BillStatus.DRAFT);

        return vendorBillRepository.save(bill);
    }

    /**
     * Step 2: post the bill into the ledger.
     * Debit  -> Expense account (Electricity / Repair) or Asset account (Pole hardware)
     * Credit -> Accounts Payable (a liability - we now owe the vendor money)
     */
    public VendorBill postBill(Long billId) {
        VendorBill bill = getById(billId);
        if (bill.getStatus() != BillStatus.DRAFT) {
            throw new InvalidOperationException("Only a DRAFT bill can be posted. Current status: " + bill.getStatus());
        }

        Journal purchaseJournal = journalRepository.findByType(JournalType.PURCHASE)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase journal not configured"));

        Account debitAccount = chooseExpenseOrAssetAccount(bill);
        Account payableAccount = accountService.getByCode(AccountCodes.ACCOUNTS_PAYABLE);

        journalPostingService.post(
                purchaseJournal,
                "BILL-" + bill.getId(),
                "Vendor bill for " + bill.getVendor().getName(),
                bill.getBillDate(),
                debitAccount,
                payableAccount,
                bill.getAmount()
        );

        bill.setStatus(BillStatus.POSTED);
        return vendorBillRepository.save(bill);
    }

    private Account chooseExpenseOrAssetAccount(VendorBill bill) {
        Product product = bill.getPurchaseOrder() == null ? null : bill.getPurchaseOrder().getProduct();
        if (product != null && product.getType() == ProductType.SERVICE) {
            return accountService.getByCode(AccountCodes.REPAIR_EXPENSES);
        }
        if (product != null && product.getName() != null && product.getName().toLowerCase().contains("kwh")) {
            return accountService.getByCode(AccountCodes.ELECTRICITY_CONSUMPTION_EXPENSE);
        }
        // Anything else (e.g. buying a physical pole assembly) increases infrastructure asset value
        return accountService.getByCode(AccountCodes.LIGHTING_GRID_INFRASTRUCTURE);
    }

    public List<VendorBill> getAll() {
        return vendorBillRepository.findAll();
    }

    public VendorBill getById(Long id) {
        return vendorBillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor bill not found: " + id));
    }

    public VendorBill save(VendorBill bill) {
        return vendorBillRepository.save(bill);
    }
}
