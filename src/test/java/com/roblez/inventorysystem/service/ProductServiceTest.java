package com.roblez.inventorysystem.service;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

import com.roblez.inventorysystem.config.ProductMapper;	
import com.roblez.inventorysystem.domain.Product;
import com.roblez.inventorysystem.dto.ProductRequest;
import com.roblez.inventorysystem.dto.UpdateStockMovementRequest;
import com.roblez.inventorysystem.repository.CategoryRepository;
import com.roblez.inventorysystem.repository.ProductRepository;
import com.roblez.inventorysystem.repository.StockMovementRepository;

public class ProductServiceTest {
	@InjectMocks
	ProductService productService;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock 
	private CategoryRepository categoryRepository;
	
	@Mock
	private ProductMapper productMapper;
	
    @Mock
    private StockMovementRepository stockMovementRepo;
	
	private ProductRequest request;
	private Product product;
	private UUID productId;

	
	@BeforeEach
	void setUp() {
		request = new ProductRequest(
			    "Laptop Gamer",                  
			    "SKU-001",                       
			    "Laptop de alto rendimiento",    
			    new BigDecimal("1500.00"),       
			    UUID.randomUUID(),               
			    "Electronics",                   
			    true                             
			);
		
        product = new Product();
        productId = UUID.randomUUID();
        product.setId(productId);
        product.setStock(5);
		
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void createProduct_ShouldThrow_WhenSkuExists() {

		when(productRepository.existsBySku("SKU-001")).thenReturn(true);
		
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> productService.createProduct(request));
        assertEquals("Ya existe el SKU", ex.getMessage());
	}
	
	@Test
	void createCategory_ShouldThrow_WhenCategoryNotFound() {

        when(productRepository.existsBySku("SKU-001")).thenReturn(false);
        when(categoryRepository.findByName("NonExistent")).thenReturn(Optional.empty());
        
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
					() -> productService.createProduct(request));
		assertEquals("No existe esa categoría", ex.getMessage());
	}
	
    @Test
    void adjustStock_ShouldThrow_WhenInsufficientStock() {
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> productService.adjustStock(productId, new UpdateStockMovementRequest(-10, "Venta")));
        assertEquals("Stock insuficiente", ex.getMessage());
    }
	
	@Test
	void adjustStock_ShouldIncreaseStock_WhenDeltaPositive() {
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        productService.adjustStock(product.getId(), new UpdateStockMovementRequest(10, "Ingreso de stock"));
	
        assertEquals(15, product.getStock());
	}

    @Test
    void adjustStock_ShouldDecreaseStock_WhenDeltaNegativeWithinLimit() {

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        productService.adjustStock(productId,  new UpdateStockMovementRequest(-2, "Venta"));

        assertEquals(3, product.getStock());
    }
}
