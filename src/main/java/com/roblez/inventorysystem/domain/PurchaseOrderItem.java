package com.roblez.inventorysystem.domain;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "purchase_order_items")
public class PurchaseOrderItem {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id", nullable = false)
    @JsonBackReference
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Min(1)
    @Positive
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    @PositiveOrZero
    private Double unitPrice;
    
    // Constructores
    public PurchaseOrderItem() {}
    
    public PurchaseOrderItem(PurchaseOrder purchaseOrder, Product product, Integer quantity, Double unitPrice) {
		this.purchaseOrder = purchaseOrder;
		this.product = product;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
	}
    
    // Utilidades
    @Transient
    public double getSubtotal() {
        Integer q = this.quantity == null ? 0 : this.quantity;
        Double up = this.unitPrice == null ? 0.0 : this.unitPrice;
        return q * up;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseOrderItem)) return false;
        PurchaseOrderItem other = (PurchaseOrderItem) o;
        if (this.id == null || other.id == null) {
            // Si alguna de las entidades no está persistida, las tratamos como distintas.
            return false;
        }
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return (id != null) ? id.hashCode() : System.identityHashCode(this);
    }
    
    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public PurchaseOrder getPurchaseOrder() { return purchaseOrder; }
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) { this.purchaseOrder = purchaseOrder; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
}
