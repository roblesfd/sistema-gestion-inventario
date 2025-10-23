package com.roblez.inventorysystem.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.roblez.inventorysystem.domain.PurchaseOrderItem;

@Repository
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, UUID> {

	Optional<PurchaseOrderItem> findByPurchaseOrderId(UUID purchaseOrderId);}
