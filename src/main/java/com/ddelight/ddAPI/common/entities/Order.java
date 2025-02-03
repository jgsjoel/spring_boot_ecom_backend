package com.ddelight.ddAPI.common.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private  User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private  Product product;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private  Invoice invoice;

    private Double sellPrice;

    private Integer quantity;

    private String orderUid;
    private LocalDateTime orderDate;

    @PostPersist
    public void generateInvoiceNumber() {
        if (this.id != null) {
            this.orderUid = "OID-" + String.format("%06d", this.id);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOrderUid() {
        return orderUid;
    }

    public void setOrderUid(String orderUid) {
        this.orderUid = orderUid;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
