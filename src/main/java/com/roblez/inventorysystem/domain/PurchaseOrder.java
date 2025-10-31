package com.roblez.inventorysystem.domain;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.roblez.inventorysystem.audit.AuditEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "purchase_orders")
@EntityListeners(AuditEntityListener.class)
public class PurchaseOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;	
	
	@Column(nullable = false, updatable = false)		
	private Instant createdAt;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)		
	private OrderStatus status = OrderStatus.PENDING;
	
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    @JsonBackReference
    private Supplier supplier;

	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User generatedBy;
	
	private Instant expectedDeliveryDate;
	
	@OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private Set<PurchaseOrderItem> items = new HashSet<>();
	
	@PositiveOrZero
	private Double total;

	
	// Constructores
	public PurchaseOrder() {}
	
	public PurchaseOrder(OrderStatus status, Supplier supplier, User generatedBy, Instant expectedDeliveryDate, Set<PurchaseOrderItem> items, double total) {
		this.status = status;
		this.supplier = supplier;
		this.generatedBy = generatedBy;
		this.expectedDeliveryDate = expectedDeliveryDate;
		this.items = items;
		this.total = total;
	}
	
	@PrePersist
	protected void onCreate() {
		this.createdAt = Instant.now();
	}
	
	// Helpers
    public void addItem(PurchaseOrderItem item) {
        items.add(item);
        item.setPurchaseOrder(this);
    }
    
    public void removeItem(PurchaseOrderItem item) {
        items.remove(item);
        item.setPurchaseOrder(null);
    }
    
	public PurchaseOrder(UUID id, Instant createdAt, OrderStatus status, Supplier supplier, User generatedBy,
			Set<PurchaseOrderItem> items) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.status = status;
		this.supplier = supplier;
		this.generatedBy = generatedBy;
		this.items = items;
	}
	
	// Getters and Setters	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public User getGeneratedBy() {
		return generatedBy;
	}

	public void setGeneratedBy(User generatedBy) {
		this.generatedBy = generatedBy;
	}
	
	public Instant getExpectedDeliveryDate() {
		return expectedDeliveryDate;
	}
	
	public void setExpectedDeliveryDate(Instant expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}

	public Set<PurchaseOrderItem> getItems() {
		return items;
	}

	public void setItems(Set<PurchaseOrderItem> items) {
		this.items = items;
	}
	
	public Double getTotal() {
		return total;
	}
	
	public void setTotal(Double total) {
		this.total = total;
	}
}
