package com.roblez.inventorysystem.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.roblez.inventorysystem.config.StockMovementMapper;
import com.roblez.inventorysystem.domain.Product;
import com.roblez.inventorysystem.domain.StockMovement;
import com.roblez.inventorysystem.dto.StockMovementResponse;
import com.roblez.inventorysystem.repository.ProductRepository;
import com.roblez.inventorysystem.repository.StockMovementRepository;


@Service
@Transactional
public class StockMovementService {
	private final StockMovementRepository stockRepo;
    private final ProductRepository productRepo;
    private final StockMovementMapper mapper;
    
	public StockMovementService(StockMovementRepository stockRepo, ProductRepository productRepo,
			StockMovementMapper mapper) {
		this.stockRepo = stockRepo;
		this.productRepo = productRepo;
		this.mapper = mapper;
	}
	

    // Registrar movimiento
    public StockMovementResponse createMovement(UUID productId, int delta, String reason) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        product.adjustStock(delta);
        
        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setDelta(delta);
        movement.setHappenedAt(Instant.now());
        movement.setReason(reason);
        StockMovement saved = stockRepo.save(movement);
        return mapper.toDto(saved);
    }
    
    // Listar todos los movimientos
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getAllMovements() {
        return stockRepo.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    // Listar movimientos por producto
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getMovementsByProduct(UUID productId) {
        return stockRepo.findByProductId(productId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    // Top 10 recientes
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getRecentMovements() {
        return stockRepo.findTop10ByOrderByHappenedAtDesc().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    // Filtrar por rango de fechas
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getMovementsByProductAndDate(UUID productId, Instant from, Instant to) {
        return stockRepo.findByProductIdAndHappenedAtBetween(productId, from, to).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getMovementsAfterDate(Instant date) {
        return stockRepo.findByHappenedAtGreaterThan(date).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getMovementsBeforeDate(Instant date) {
        return stockRepo.findByHappenedAtLessThanEqual(date).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    // Obtener movimientos mayores o iguales a delta (cantidad)
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getMovementsGreaterThanDelta(Integer delta) {
        return stockRepo.findByDeltaGreaterThanEqual(delta).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
