package com.roblez.inventorysystem.audit;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditEntryRepository extends JpaRepository<AuditEntry, UUID> {}