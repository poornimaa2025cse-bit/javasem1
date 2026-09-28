package com.citygrid.smartlighting.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentType type;   // VENDOR_PAYMENT or CUSTOMER_RECEIPT

    @ManyToOne
    @JoinColumn(name = "vendor_bill_id")
    private VendorBill vendorBill;

    @ManyToOne
    @JoinColumn(name = "customer_invoice_id")
    private CustomerInvoice customerInvoice;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private Account bankAccount;   // the Cash/Bank account used

    private Double amount;
    private LocalDate paymentDate;

    public Payment() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PaymentType getType() { return type; }
    public void setType(PaymentType type) { this.type = type; }

    public VendorBill getVendorBill() { return vendorBill; }
    public void setVendorBill(VendorBill vendorBill) { this.vendorBill = vendorBill; }

    public CustomerInvoice getCustomerInvoice() { return customerInvoice; }
    public void setCustomerInvoice(CustomerInvoice customerInvoice) { this.customerInvoice = customerInvoice; }

    public Account getBankAccount() { return bankAccount; }
    public void setBankAccount(Account bankAccount) { this.bankAccount = bankAccount; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
}
