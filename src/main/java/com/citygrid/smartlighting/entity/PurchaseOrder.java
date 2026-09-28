package com.citygrid.smartlighting.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Contact vendor;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "analytic_account_id")
    private AnalyticAccount analyticAccount;   // which zone this spend belongs to

    private Double quantity;
    private Double unitPrice;
    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public PurchaseOrder() {}

    public Double getAmount() {
        return (quantity == null ? 0.0 : quantity) * (unitPrice == null ? 0.0 : unitPrice);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Contact getVendor() { return vendor; }
    public void setVendor(Contact vendor) { this.vendor = vendor; }

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
