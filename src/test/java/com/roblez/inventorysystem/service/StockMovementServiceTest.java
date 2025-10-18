package com.roblez.inventorysystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.roblez.inventorysystem.config.StockMovementMapper;
import com.roblez.inventorysystem.domain.Product;
import com.roblez.inventorysystem.domain.StockMovement;
import com.roblez.inventorysystem.dto.StockMovementResponse;
import com.roblez.inventorysystem.repository.ProductRepository;
import com.roblez.inventorysystem.repository.StockMovementRepository;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

public class StockMovementServiceTest {
    @InjectMocks
    private StockMovementService stockMovementService;

    @Mock
    private StockMovementRepository stockMovementRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private StockMovementMapper mapper;
    
	private Product product;
	UUID productId;

    @BeforeEach
    void setUp() {
    	product = new Product();
        productId = UUID.randomUUID();
        product.setId(productId);
    	product.setStock(10);
    	
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void createMovement_ShouldSaveMovementCorrectly() {

    	when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        StockMovement movement = new StockMovement(product, 5, null, "Ingreso");  
        when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(movement);
        when(mapper.toDto(any(StockMovement.class)))
        	.thenAnswer(inv -> new StockMovementResponse(((StockMovement)inv.getArgument(0)).getId(),
                                                ((StockMovement)inv.getArgument(0)).getProduct().getId(),
                                                ((StockMovement)inv.getArgument(0)).getDelta(),
										        ((StockMovement)inv.getArgument(0)).getHappenedAt(),
										        ((StockMovement)inv.getArgument(0)).getReason()));

        StockMovementResponse result = stockMovementService.createMovement(product.getId(), 5, "Ingreso");
        
        assertNotNull(result);
        assertEquals(5, result.delta());
        assertEquals("Ingreso", result.reason());
    }
    
    @Test
    void createMovement_ShouldThrow_WhenDeltaNegativeAndInsufficientStock() {
    	
    	when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> stockMovementService.createMovement(productId, -15, "Salida"));
        assertEquals("Stock insuficiente", ex.getMessage());
    }
}
