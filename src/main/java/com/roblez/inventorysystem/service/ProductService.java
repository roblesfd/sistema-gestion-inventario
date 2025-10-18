package com.roblez.inventorysystem.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.roblez.inventorysystem.config.ProductMapper;
import com.roblez.inventorysystem.domain.Category;
import com.roblez.inventorysystem.domain.Product;
import com.roblez.inventorysystem.dto.ProductRequest;
import com.roblez.inventorysystem.dto.ProductResponse;
import com.roblez.inventorysystem.dto.UpdateStockMovementRequest;
import com.roblez.inventorysystem.repository.CategoryRepository;
import com.roblez.inventorysystem.repository.ProductRepository;

@Service
@Transactional
public class ProductService {
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ProductMapper mapper;
	
	public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository,
			ProductMapper productMapper) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.mapper = productMapper;
	}
	
	//Crear producto
	public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new IllegalArgumentException("Ya existe el SKU");
        }	
		Product product = mapper.toEntity(request);
		
		if(request.categoryId() != null) {
			Category category = categoryRepository.findById(request.categoryId())
					.orElseThrow(() -> new IllegalArgumentException("No existe esa categoría"));
			product.setCategory(category);
		} else if(request.categoryName() != null) {
			Category category = categoryRepository.findByName(request.categoryName())
					.orElseGet(() -> {
						Category c = new Category();
						c.setName(request.categoryName());
						return categoryRepository.save(c);
					});
			product.setCategory(category);
		}
		
		Product saved = productRepository.save(product);
		
		return mapper.toDto(saved);
	}
	
	//Actualizar producto
	public ProductResponse updateProduct(UUID id, ProductRequest request) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
		
		product.setName(request.name());
		product.setSku(request.sku());
		product.setDescription(request.description());
		product.setPrice(request.price());
		product.setStock(request.stock());
		
		if(request.active() != null) product.setActive(request.active());
		
		if(request.categoryId() != null) {
			@SuppressWarnings("deprecation")
			Category category = categoryRepository.findById(request.categoryId()).orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
			
			product.setCategory(category);
		}else if(request.categoryName() != null) {
			Category category = categoryRepository.findByName(request.categoryName())
					.orElseGet(() -> {
						Category c = new Category();
						c.setName(request.categoryName());
						return categoryRepository.save(c);
					});
			product.setCategory(category);
		}
		
		Product updated = productRepository.save(product);
		return mapper.toDto(updated);
	}
	
	// Ajustar stock
	public ProductResponse adjustStock(UUID id, UpdateStockMovementRequest request) {
		Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
		product.adjustStock(request.delta());
		Product updated = productRepository.save(product);
		
		return mapper.toDto(updated);
	}
	
	// Obtener producto por ID
	@Transactional(readOnly= true)
	public ProductResponse getProductById(UUID id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
		return mapper.toDto(product);
	}
	
	//Listar todos los productos
	@Transactional(readOnly=true)
	public List<ProductResponse> getAllProducts() {
		return productRepository.findAll().stream()
				.map(mapper::toDto)
				.collect(Collectors.toList());
	}
	
	//Listar productos activos
	public List<ProductResponse> getActiveProducts() {
		return productRepository.findByActiveTrue().stream()
				.map(mapper::toDto)
				.collect(Collectors.toList());
	}
	
	//Listar productos por categorías
	@Transactional(readOnly = true)
	public List<ProductResponse> getProductsByCategory(Category category){
		return productRepository.findByCategory(category).stream()
				.map(mapper::toDto)
				.collect(Collectors.toList());
	}
	
	// Eliminar producto (desactivarlo)
	public void deactivateProduct(UUID id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No existe un producto con ese id"));
		
		product.setActive(false);
		productRepository.save(product);
	}
	
	// Buscar por nombre
	@Transactional(readOnly=true)
	public List<ProductResponse> searchByName(String name) {
		return productRepository.findByNameContainingIgnoreCase(name).stream()
				.map(mapper::toDto).collect(Collectors.toList());
	}
	
	// Filtrar por stock bajo
	@Transactional(readOnly=true)
	public List<ProductResponse> getProductsLowStock(int threshold) {
		return productRepository.findByStockLessThan(threshold).stream()
				.map(mapper::toDto).collect(Collectors.toList());
	}
	
	// Filtrar por rango de precio
	public List<ProductResponse> getProductsByPriceRange(Double min, Double max) {
		return productRepository.findByPriceBetween(min, max).stream()
				.map(mapper::toDto).collect(Collectors.toList());
	}
	
	
	
}
