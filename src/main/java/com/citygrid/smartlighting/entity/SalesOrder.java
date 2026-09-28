package com.citygrid.smartlighting.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sales_orders")
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Contact customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "analytic_account_id")
    private AnalyticAccount analyticAccount;

    private Double quantity;
    private Double unitPrice;
    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public SalesOrder() {}

    public Double getAmount() {
        return (quantity == null ? 0.0 : quantity) * (unitPrice == null ? 0.0 : unitPrice);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Contact getCustomer() { return customer; }
    public void setCustomer(Contact customer) { this.customer = customer; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public AnalyticAccount getAnalyticAccount() { return analyticAccount; }
    public void setAnalyticAccount(AnalyticAccount analyticAccount) { this.analyticAccount = analyticAccount; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
}
