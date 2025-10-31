package com.roblez.inventorysystem.audit;

import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
	@CreatedDate
	@Column(updatable =  false)
	private Instant createdAt;
	
	@LastModifiedDate
	private Instant modifiedAt;
	
	@CreatedBy
	@Column(updatable=false)
	private String createdBy;
	
	@LastModifiedBy
	private String modifiedBy;
}
