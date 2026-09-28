package com.citygrid.smartlighting.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "customer_invoices")
public class CustomerInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sales_order_id")
    private SalesOrder salesOrder;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Contact customer;

    @ManyToOne
    @JoinColumn(name = "analytic_account_id")
    private AnalyticAccount analyticAccount;

    private Double amount;
    private LocalDate invoiceDate;

    @Enumerated(EnumType.STRING)
    private BillStatus status;   // DRAFT -> POSTED -> PAID

    public CustomerInvoice() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public SalesOrder getSalesOrder() { return salesOrder; }
    public void setSalesOrder(SalesOrder salesOrder) { this.salesOrder = salesOrder; }

    public Contact getCustomer() { return customer; }
    public void setCustomer(Contact customer) { this.customer = customer; }

    public AnalyticAccount getAnalyticAccount() { return analyticAccount; }
    public void setAnalyticAccount(AnalyticAccount analyticAccount) { this.analyticAccount = analyticAccount; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }

    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus status) { this.status = status; }
}
