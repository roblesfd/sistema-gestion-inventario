package com.roblez.inventorysystem.audit;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_entries", indexes = {
    @Index(name = "idx_audit_entity_ts", columnList = "entity_name,created_at")
})
public class AuditEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="entity_name", nullable=false)
    private String entityName;

    @Column(name="entity_id", nullable=true, columnDefinition="varchar(100)")
    private String entityId;

    @Column(nullable=false)
    private String action; // CREATE, UPDATE, DELETE

    @Column(nullable=true)
    private String username;

    @Column(name="created_at", nullable=false)
    private Instant createdAt = Instant.now();

    @Lob
    @Column(name="before_state", columnDefinition = "text")
    private String beforeState;

    @Lob
    @Column(name="after_state", columnDefinition = "text")
    private String afterState;

    @Lob
    @Column(name="diff", columnDefinition = "text")
    private String diff; // campos cambiados
    
    
    // Constructor vacío (obligatorio para JPA)
    public AuditEntry() {}

    // Constructor con todos los campos
	public AuditEntry(String entityName, String entityId, String action, String username, Instant createdAt,
			String beforeState, String afterState, String diff) {
		this.entityName = entityName;
		this.entityId = entityId;
		this.action = action;
		this.username = username;
		this.createdAt = createdAt;
		this.beforeState = beforeState;
		this.afterState = afterState;
		this.diff = diff;
	}

 
    // Getters y Setters
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public String getBeforeState() {
		return beforeState;
	}

	public void setBeforeState(String beforeState) {
		this.beforeState = beforeState;
	}

	public String getAfterState() {
		return afterState;
	}

	public void setAfterState(String afterState) {
		this.afterState = afterState;
	}

	public String getDiff() {
		return diff;
	}

	public void setDiff(String diff) {
		this.diff = diff;
	}
}
