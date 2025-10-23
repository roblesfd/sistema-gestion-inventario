package com.roblez.inventorysystem.repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.roblez.inventorysystem.domain.StockMovement;
import com.roblez.inventorysystem.dto.UpdateStockMovementRequest;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {
	StockMovement save(StockMovement movement);
	List<StockMovement> findByProductId(UUID id);
	long countByProductId(UUID id);
	List<StockMovement> findByProductIdAndHappenedAtBetween(UUID id, Instant from, Instant to);
	List<StockMovement> findByDeltaGreaterThanEqual(Integer delta);
	List<StockMovement> findTop10ByOrderByHappenedAtDesc();
	long countByProductIdAndDelta(UUID id, Integer delta);
	List<StockMovement> findByHappenedAtGreaterThan(Instant after);
	List<StockMovement> findByHappenedAtLessThanEqual(Instant before);
}
