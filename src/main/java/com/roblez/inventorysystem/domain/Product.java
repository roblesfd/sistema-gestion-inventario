package com.roblez.inventorysystem.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="products", uniqueConstraints=@UniqueConstraint(name="uk_product_sku", columnNames="sku"))
public class Product {
	
	@Id
	@Column(columnDefinition="uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@NotBlank
	@Column(nullable=false)
	private String name;
	
	@NotBlank
	@Column(nullable=false, unique=true)
	private String sku;
	
	@Column(length=2000)
	private String description;
	
	@NotNull
	@Min(0)
	@Column(nullable=false, scale=2, precision=12)
	private BigDecimal price = BigDecimal.ZERO;
	
	@NotNull
	@Min(0)
	@Column(nullable=false)
	private Integer stock = 0;
	
	@Min(30)
	@Column(nullable=true)
	private Integer minStock = 30;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="category_id")
	@JsonBackReference
	private Category category;
	
	@Column
	private boolean active = false;
	
	@Column(nullable=false, updatable=false)
	private Instant createdAt;
	
	@Column(nullable=false)
	private Instant updatedAt;
	
	@Version
	private Long version; //optimistic locking
	
	@PrePersist
	public void prePresist() {
		Instant now = Instant.now();
		this.createdAt = now;
		this.updatedAt = now;
	}
	
	@PreUpdate
	public void preUpdate() {
		Instant now = Instant.now();
		this.updatedAt = now;
	}
	
	//Metodos utils
	public void adjustStock(int delta) {
		int nuevo = (this.stock == null ? 0 : this.stock) + delta;
		if(nuevo < 0) throw new IllegalArgumentException("Stock insuficiente");
		this.stock = nuevo;
	}
	
	//Constructores
	public Product() {}

	// Constructor para requests
	public Product(@NotBlank String name, @NotBlank String sku, @NotNull @Min(0) BigDecimal price, @NotNull @Min(0) Integer stock,
			Category category) {
		super();
		this.name = name;
		this.sku = sku;
		this.price = price;
		this.stock = stock;
		this.category = category;
	}

	// Constructor para responses
	public Product(@NotBlank String name, @NotBlank String sku, String description, @NotNull @Min(0) BigDecimal price,
			@NotNull @Min(0) Integer stock, Category category, boolean active) {
		this.name = name;
		this.sku = sku;
		this.description = description;
		this.price = price;
		this.stock = stock;
		this.category = category;
		this.active = active;
	}
	
	//Getters y Setters
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}
	
	public Integer getMinStock() {
		if (minStock == null) {
			return 30;
		}
		return minStock;
	}

	public void setMinStock(Integer stock) {
		this.minStock = stock;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	public boolean getActive() {
		return this.active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
}
