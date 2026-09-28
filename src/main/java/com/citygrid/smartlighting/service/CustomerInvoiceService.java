package com.citygrid.smartlighting.service;

import com.citygrid.smartlighting.config.AccountCodes;
import com.citygrid.smartlighting.entity.*;
import com.citygrid.smartlighting.exception.InvalidOperationException;
import com.citygrid.smartlighting.exception.ResourceNotFoundException;
import com.citygrid.smartlighting.repository.CustomerInvoiceRepository;
import com.citygrid.smartlighting.repository.JournalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Handles the Order-to-Cash cycle:
 *   Sales Order  ->  Customer Invoice (this class)  ->  Payment Receipt (see PaymentService)
 */
@Service
public class CustomerInvoiceService {

    private final CustomerInvoiceRepository customerInvoiceRepository;
    private final SalesOrderService salesOrderService;
    private final JournalRepository journalRepository;
    private final JournalPostingService journalPostingService;
    private final AccountService accountService;

    public CustomerInvoiceService(CustomerInvoiceRepository customerInvoiceRepository,
                                   SalesOrderService salesOrderService,
                                   JournalRepository journalRepository,
                                   JournalPostingService journalPostingService,
                                   AccountService accountService) {
        this.customerInvoiceRepository = customerInvoiceRepository;
        this.salesOrderService = salesOrderService;
        this.journalRepository = journalRepository;
        this.journalPostingService = journalPostingService;
        this.accountService = accountService;
    }

    public CustomerInvoice createFromSalesOrder(Long salesOrderId) {
        SalesOrder so = salesOrderService.getById(salesOrderId);

        CustomerInvoice invoice = new CustomerInvoice();
        invoice.setSalesOrder(so);
        invoice.setCustomer(so.getCustomer());
        invoice.setAnalyticAccount(so.getAnalyticAccount());
        invoice.setAmount(so.getAmount());
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setStatus(BillStatus.DRAFT);

        return customerInvoiceRepository.save(invoice);
    }

    /**
     * Debit  -> Accounts Receivable (customer now owes us money)
     * Credit -> Municipal Grid Service Fee (Income)
     */
    public CustomerInvoice postInvoice(Long invoiceId) {
        CustomerInvoice invoice = getById(invoiceId);
        if (invoice.getStatus() != BillStatus.DRAFT) {
            throw new InvalidOperationException("Only a DRAFT invoice can be posted. Current status: " + invoice.getStatus());
        }

        Journal salesJournal = journalRepository.findByType(JournalType.SALES)
                .orElseThrow(() -> new ResourceNotFoundException("Sales journal not configured"));

        Account receivable = accountService.getByCode(AccountCodes.ACCOUNTS_RECEIVABLE);
        Account incomeAccount = accountService.getByCode(AccountCodes.MUNICIPAL_GRID_SERVICE_FEE);

        journalPostingService.post(
                salesJournal,
                "INV-" + invoice.getId(),
                "Customer invoice for " + invoice.getCustomer().getName(),
                invoice.getInvoiceDate(),
                receivable,
                incomeAccount,
                invoice.getAmount()
        );

        invoice.setStatus(BillStatus.POSTED);
        return customerInvoiceRepository.save(invoice);
    }

    public List<CustomerInvoice> getAll() {
        return customerInvoiceRepository.findAll();
    }

    public CustomerInvoice getById(Long id) {
        return customerInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer invoice not found: " + id));
    }

    public CustomerInvoice save(CustomerInvoice invoice) {
        return customerInvoiceRepository.save(invoice);
    }
}
