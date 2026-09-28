package com.citygrid.smartlighting.service;

import com.citygrid.smartlighting.config.AccountCodes;
import com.citygrid.smartlighting.dto.PaymentRequestDTO;
import com.citygrid.smartlighting.entity.*;
import com.citygrid.smartlighting.exception.InvalidOperationException;
import com.citygrid.smartlighting.exception.ResourceNotFoundException;
import com.citygrid.smartlighting.repository.JournalRepository;
import com.citygrid.smartlighting.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * The final step of both money cycles:
 *   Vendor Bill      -> pay via Bank      (reduces our Accounts Payable, reduces Bank)
 *   Customer Invoice -> receive via Bank  (increases Bank, reduces Accounts Receivable)
 */
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final VendorBillService vendorBillService;
    private final CustomerInvoiceService customerInvoiceService;
    private final JournalRepository journalRepository;
    private final JournalPostingService journalPostingService;
    private final AccountService accountService;

    public PaymentService(PaymentRepository paymentRepository,
                           VendorBillService vendorBillService,
                           CustomerInvoiceService customerInvoiceService,
                           JournalRepository journalRepository,
                           JournalPostingService journalPostingService,
                           AccountService accountService) {
        this.paymentRepository = paymentRepository;
        this.vendorBillService = vendorBillService;
        this.customerInvoiceService = customerInvoiceService;
        this.journalRepository = journalRepository;
        this.journalPostingService = journalPostingService;
        this.accountService = accountService;
    }

    public Payment payVendorBill(Long billId, PaymentRequestDTO dto) {
        VendorBill bill = vendorBillService.getById(billId);
        if (bill.getStatus() != BillStatus.POSTED) {
            throw new InvalidOperationException("Only a POSTED bill can be paid. Current status: " + bill.getStatus());
        }

        Journal bankJournal = journalRepository.findByType(JournalType.BANK)
                .orElseThrow(() -> new ResourceNotFoundException("Bank journal not configured"));

        Account payableAccount = accountService.getByCode(AccountCodes.ACCOUNTS_PAYABLE);
        Account bankAccount = accountService.getById(dto.getBankAccountId());
        LocalDate paymentDate = dto.getPaymentDate() == null ? LocalDate.now() : dto.getPaymentDate();
        double amount = dto.getAmount() == null ? bill.getAmount() : dto.getAmount();

        // Debit Accounts Payable (liability goes down) / Credit Bank (asset goes down)
        journalPostingService.post(
                bankJournal,
                "PAY-BILL-" + bill.getId(),
                "Payment to vendor " + bill.getVendor().getName(),
                paymentDate,
                payableAccount,
                bankAccount,
                amount
        );

        Payment payment = new Payment();
        payment.setType(PaymentType.VENDOR_PAYMENT);
        payment.setVendorBill(bill);
        payment.setBankAccount(bankAccount);
        payment.setAmount(amount);
        payment.setPaymentDate(paymentDate);
        paymentRepository.save(payment);

        bill.setStatus(BillStatus.PAID);
        vendorBillService.save(bill);
        return payment;
    }

    public Payment receiveCustomerPayment(Long invoiceId, PaymentRequestDTO dto) {
        CustomerInvoice invoice = customerInvoiceService.getById(invoiceId);
        if (invoice.getStatus() != BillStatus.POSTED) {
            throw new InvalidOperationException("Only a POSTED invoice can receive payment. Current status: " + invoice.getStatus());
        }

        Journal bankJournal = journalRepository.findByType(JournalType.BANK)
                .orElseThrow(() -> new ResourceNotFoundException("Bank journal not configured"));

        Account receivableAccount = accountService.getByCode(AccountCodes.ACCOUNTS_RECEIVABLE);
        Account bankAccount = accountService.getById(dto.getBankAccountId());
        LocalDate paymentDate = dto.getPaymentDate() == null ? LocalDate.now() : dto.getPaymentDate();
        double amount = dto.getAmount() == null ? invoice.getAmount() : dto.getAmount();

        // Debit Bank (asset goes up) / Credit Accounts Receivable (asset goes down)
        journalPostingService.post(
                bankJournal,
                "RCPT-INV-" + invoice.getId(),
                "Receipt from " + invoice.getCustomer().getName(),
                paymentDate,
                bankAccount,
                receivableAccount,
                amount
        );

        Payment payment = new Payment();
        payment.setType(PaymentType.CUSTOMER_RECEIPT);
        payment.setCustomerInvoice(invoice);
        payment.setBankAccount(bankAccount);
        payment.setAmount(amount);
        payment.setPaymentDate(paymentDate);
        paymentRepository.save(payment);

        invoice.setStatus(BillStatus.PAID);
        customerInvoiceService.save(invoice);
        return payment;
    }
}
