package com.roblez.inventorysystem.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="categories")
public class Category {
	@Id
	@Column(columnDefinition="uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(nullable=false, unique=true)
	private String name;
	
	@OneToMany(mappedBy="category", fetch=FetchType.LAZY)
	private List<Product> products = new ArrayList<>();
	
	@Column
	private boolean active = true;
	
	//Constructores
	public Category() {}

	public Category(String name) { this.name=name;}
	
	public Category(String name, boolean active) { 
		this.name=name;
		this.active=active;
	}

	
	//Getters y Setters
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	public boolean getActive() {
		return this.active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
}
