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
import jakarta.persistence.PrePersist;


@Entity
@Table(name = "alert_events", indexes = {
    @Index(name = "idx_alert_sentat", columnList = "sent_at")
})
public class AlertEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(length = 500)
    private String message;

    @Column(name = "success", nullable = false)
    private boolean success;

    @Column(name = "sent_at", nullable = false, updatable=false)
    private Instant sentAt;
    
    public AlertEvent() {}

    public AlertEvent(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    
    @PrePersist
    public void prePersist() {
    	this.sentAt = Instant.now();
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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
}
