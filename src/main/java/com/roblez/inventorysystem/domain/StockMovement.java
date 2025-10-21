package com.roblez.inventorysystem.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="stock_movements")
public class StockMovement {
	@Id
	@Column(columnDefinition="uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="product_id", nullable=false)
	private Product product;
	
	@NotNull
	private Integer delta; // positivo entrada, negativo salida
	
	@Column(nullable=false)
	private Instant happenedAt = Instant.now();
	
	@Column(length=1000)
	private String reason;
	
	//Constructores
	public StockMovement() {}

	public StockMovement(Product product, @NotNull Integer delta, String reason) {
		this.product = product;
		this.delta = delta;
		this.reason = reason;
	}
	
	//Getters y Setters
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getDelta() {
		return delta;
	}

	public void setDelta(Integer delta) {
		this.delta = delta;
	}

	public Instant getHappenedAt() {
		return happenedAt;
	}

	public void setHappenedAt(Instant happenedAt) {
		this.happenedAt = happenedAt;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
