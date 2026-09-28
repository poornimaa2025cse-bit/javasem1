package com.citygrid.smartlighting.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class PaymentRequestDTO {

    @NotNull
    private Double amount;

    private LocalDate paymentDate;

    @NotNull
    private Long bankAccountId;   // which Cash/Bank Account (from Chart of Accounts) is used

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public Long getBankAccountId() { return bankAccountId; }
    public void setBankAccountId(Long bankAccountId) { this.bankAccountId = bankAccountId; }
}
