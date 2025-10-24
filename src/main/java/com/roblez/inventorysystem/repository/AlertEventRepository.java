package com.roblez.inventorysystem.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.roblez.inventorysystem.domain.AlertEvent;

@Repository
public interface AlertEventRepository extends JpaRepository<AlertEvent, UUID>{
	
	// Obtener eventos recientes para un producto y numero
	List<AlertEvent> findByProductIdAndPhoneNumberAndSentAtAfter(UUID id, String phoneNumber, Instant sentAt);
	
	// Obtener ultimo evento para dedupe rapido
	AlertEvent findTopByProductIdAndPhoneNumberOrderBySentAtDesc(UUID productId, String phoneNumber);
	
	// contar fallos recientes
	long countByPhoneNumberAndSuccessIsFalseAndSentAtAfter(String phoneNumber, Instant after);
}
