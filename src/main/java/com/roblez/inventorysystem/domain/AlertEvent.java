package com.roblez.inventorysystem.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;


@Entity
@Table(name = "alert_events", indexes = {
    @Index(name = "idx_alert_product_phone", columnList = "product_id, phone_number"),
    @Index(name = "idx_alert_sentat", columnList = "sent_at")
})
public class AlertEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @Column(name = "product_id", columnDefinition = "uuid", nullable = true)
    private UUID productId;

    @Column(name = "phone_number", length = 30, nullable = false)
    private String phoneNumber;

    @Column(length = 500)
    private String message;

    @Column(name = "success", nullable = false)
    private boolean success;

    @Column(name = "sent_at", nullable = false)
    private Instant sentAt;
    
    public AlertEvent() {}

    public AlertEvent(UUID productId, String phoneNumber, String message, boolean success, Instant sentAt) {
        this.productId = productId;
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.success = success;
        this.sentAt = sentAt;
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Instant getSentAt() {
		return sentAt;
	}

	public void setSentAt(Instant sentAt) {
		this.sentAt = sentAt;
	}
}
