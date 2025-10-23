package com.roblez.inventorysystem.web;

import java.util.List;
import java.util.Set;
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

import io.swagger.v3.oas.annotations.Operation;
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
	
	// Obtener todos
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> response = productService.getAllProducts();
        return ResponseEntity.ok(response);
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
	
	// Ajustar stock
	@PatchMapping("/{id}/stock")
    @Operation(summary = "Ajustar stock de producto", description = "Aumenta o disminuye el valor de 'delta' el cual representa el stock del producto")
	public ResponseEntity<@Valid ProductResponse> adjustStock(
				@PathVariable UUID id,
				@Valid @RequestBody UpdateStockMovementRequest request
			) {
		ProductResponse response = productService.adjustStock(id, request);
		return ResponseEntity.ok(response);
	}
	
    // Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }
    
    // Obtener producto por SKU
    @GetMapping("/sku/{sku}")
    @Operation(summary = "Obtener por SKU", description = "Obtiene un producto a partir de su clave SKU")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String sku) {
        ProductResponse response = productService.getProductBySku(sku);
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
    @Operation(summary = "Buscar por nombre", description = "Busca productos por su nombre")
    public ResponseEntity<List<ProductResponse>> searchByName(@RequestParam String name) {
    	List<ProductResponse> list = productService.searchByName(name);
    	return ResponseEntity.ok(list);
    }
    
    // Filtrar por stock bajo
    @GetMapping("/low-stock")
    @Operation(summary = "Obtene por stock bajo", description = "Obtiene todos los productos que se encuentren por debajo de un valor (threshold)")
    public ResponseEntity<List<ProductResponse>> getProductsLowStock(@RequestParam int threshold) {
    	List<ProductResponse> list = productService.getProductsLowStock(threshold);
    	return ResponseEntity.ok(list);
    }
    
    // Filtrar por rango de precios
    @GetMapping("/price-range")
    @Operation(summary = "Obtener por rango de precios", description = "Obtiene productos que se encuentren dentro de un rango (min, max) definido")
    public ResponseEntity<List<ProductResponse>> getProductsByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<ProductResponse> list = productService.getProductsByPriceRange(min, max);
        return ResponseEntity.ok(list);
    }
    
    // Desactivar producto (borrado lógico)
    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar un producto", description = "Desactiva un producto cambiando el valor de active=false. Reactivarlo con PUT /api/products/{id}")
    public ResponseEntity<Void> deactivateProduct(@PathVariable UUID id) {
        productService.deactivateProduct(id);
        return ResponseEntity.noContent().build();
    }
}
