package com.roblez.inventorysystem.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.roblez.inventorysystem.domain.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

	Optional<Supplier> findByContactEmail(String contactEmail);

}
