package com.roblez.inventorysystem.repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roblez.inventorysystem.domain.StockMovement;

public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {
	
	List<StockMovement> findByProductId(UUID id);
	long countByProductId(UUID id);
	List<StockMovement> findByProductIdAndHappenedAtBetween(UUID id, Instant from, Instant to);
	List<StockMovement> findByDelta(Integer delta);
	List<StockMovement> findTop10ByOrderByHappenedAtDesc();
	long countByProductIdAndDelta(UUID id, Integer delta);
	List<StockMovement> findByHappenedAtAfter(Instant after);
}
