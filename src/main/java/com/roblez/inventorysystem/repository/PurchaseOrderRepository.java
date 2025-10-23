package com.roblez.inventorysystem.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.roblez.inventorysystem.domain.OrderStatus;
import com.roblez.inventorysystem.domain.PurchaseOrder;
import com.roblez.inventorysystem.domain.Supplier;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {
	List<PurchaseOrder> findBySupplierId(UUID supplierId);
	List<PurchaseOrder> findByStatus(OrderStatus status);
	List<PurchaseOrder> findByGeneratedById(UUID userId);
	List<PurchaseOrder> findByCreatedAtBetween(Instant start, Instant end);
	List<PurchaseOrder> findByStatusIn(List<OrderStatus> statuses);
	long countBySupplierIdAndStatusIn(UUID supplierId, List<OrderStatus> statuses);
	// Obtener todas las ordenes de un producto
	@Query("SELECT po FROM PurchaseOrder po JOIN po.items item WHERE item.product.id = :productId")
	List<PurchaseOrder> findByProductId(@Param("productId") UUID productId);
	List<PurchaseOrder> findByIdIn(Set<UUID> itemRequests);
}
