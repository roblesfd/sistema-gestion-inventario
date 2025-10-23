package com.roblez.inventorysystem.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "suppliers")
public class Supplier {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "full_name", nullable = false)
	private String fullName;
	
	@Column(name = "contact_email", nullable = false)	
	@Email
	private String contactEmail;
	
	@Column(name="phone_number", nullable = false)
	private String phoneNumber;
	
    @OneToMany(mappedBy = "supplier")
    @JsonManagedReference 
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();
    
	// Constructores
	public Supplier() {}
	
	public Supplier(String fullName, String contactEmail, String phoneNumber) {
		this.fullName = fullName;
		this.contactEmail = contactEmail;
		this.phoneNumber = phoneNumber;
	}

	// Getters and Setters
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Set<PurchaseOrder> getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}
}
