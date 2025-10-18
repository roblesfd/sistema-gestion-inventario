package com.roblez.inventorysystem.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roblez.inventorysystem.dto.ProductRequest;
import com.roblez.inventorysystem.dto.ProductResponse;
import com.roblez.inventorysystem.dto.UpdateStockMovementRequest;
import com.roblez.inventorysystem.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	private final ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	//Crear producto
	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
		ProductResponse response = productService.createProduct(request);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	// Actualizar producto
	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> updateProduct(
			@PathVariable UUID id,
			@Valid @RequestBody ProductRequest request 
			) {
		ProductResponse response = productService.updateProduct(id, request);
		
		return ResponseEntity.ok(response);
	}
	
//	// Ajustar stock
//	@PatchMapping("/{id}/stock")
//	public ResponseEntity<ProductResponse> adjustStock(
//				@PathVariable UUID id,
//				@Valid @RequestBody UpdateStockMovementRequest request
//			) {
//		ProductResponse response = productService.adjustStock(id, request);
//		return ResponseEntity.ok(request);
//	}
	
    // Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }
    
    // Listar los productos activos
    @GetMapping("/active")
    public ResponseEntity<List<ProductResponse>> getActiveProducts() {
    	List<ProductResponse> list = productService.getActiveProducts();
    	return ResponseEntity.ok(list);
    }
    
    // Buscar productos por nombre
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchByName(@RequestParam String name) {
    	List<ProductResponse> list = productService.searchByName(name);
    	return ResponseEntity.ok(list);
    }
    
    // Filtrar por stock bajo
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponse>> getProductsLowStock(@RequestParam int threshold) {
    	List<ProductResponse> list = productService.getProductsLowStock(threshold);
    	return ResponseEntity.ok(list);
    }
    
    // Filtrar por rango de precios
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponse>> getProductsByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<ProductResponse> list = productService.getProductsByPriceRange(min, max);
        return ResponseEntity.ok(list);
    }

    // Desactivar producto (borrado lógico)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateProduct(@PathVariable UUID id) {
        productService.deactivateProduct(id);
        return ResponseEntity.noContent().build();
    }
}
