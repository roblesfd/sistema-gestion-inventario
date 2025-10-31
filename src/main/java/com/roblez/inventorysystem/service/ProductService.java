package com.roblez.inventorysystem.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.roblez.inventorysystem.alert.AlertSender;
import com.roblez.inventorysystem.alert.EmailProperties;
import com.roblez.inventorysystem.config.ProductMapper;
import com.roblez.inventorysystem.domain.Category;
import com.roblez.inventorysystem.domain.Product;
import com.roblez.inventorysystem.domain.StockMovement;
import com.roblez.inventorysystem.dto.ProductRequest;
import com.roblez.inventorysystem.dto.ProductResponse;
import com.roblez.inventorysystem.dto.UpdateStockMovementRequest;
import com.roblez.inventorysystem.repository.CategoryRepository;
import com.roblez.inventorysystem.repository.ProductRepository;
import com.roblez.inventorysystem.repository.StockMovementRepository;

@Service
@Transactional
public class ProductService {
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final StockMovementRepository stockMovementRepo;
	private final ProductMapper mapper;
	private final AlertSender alertSender;
	private final String emailRecipient;
	private final Logger log = LoggerFactory.getLogger(ProductService.class);
	
	public ProductService(
			ProductRepository productRepository, 
			CategoryRepository categoryRepository,
			StockMovementRepository stockRepo, 
			ProductMapper productMapper, 
			AlertSender alertSender,
			EmailProperties emailProperties
			) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.stockMovementRepo = stockRepo;
		this.mapper = productMapper;
		this.alertSender = alertSender;
		this.emailRecipient = emailProperties.getRecipient();
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
						return categoryRepository.save(new Category(request.categoryName()));
					});
			product.setCategory(category);
		}
		
		Product saved = productRepository.save(product);
		
		log.info("Producto creado -> ID: {}, Nombre: {}, SKU: {} ", saved.getId(), saved.getName(), saved.getSku());
		
		return mapper.toDto(saved);
	}
	
	//Actualizar producto
	// Si se quiere actualizar stock, con metodo adjustStock unicamente
	public ProductResponse updateProduct(UUID id, ProductRequest request) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
		
		product.setName(request.name());
		product.setSku(request.sku());
		product.setDescription(request.description());
		product.setPrice(request.price());
		
		if(request.active() != null) product.setActive(request.active());
		
		if(request.categoryId() != null) {
			Category category = categoryRepository.findById(request.categoryId()).orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));			
			product.setCategory(category);
		}else if(request.categoryName() != null) {
			Category category = categoryRepository.findByName(request.categoryName())
					.orElseGet(() -> {
						return categoryRepository.save(new Category(request.categoryName()));
					});
			product.setCategory(category);
		}
		
		Product updated = productRepository.save(product);
		log.info("Producto actualizado -> ID: {}, Nombre: {}, SKU: {} ", updated.getId(), updated.getName(), updated.getSku());

		return mapper.toDto(updated);
	}
	
	// Ajustar stock
	public ProductResponse adjustStock(UUID id, UpdateStockMovementRequest request) {
		Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
		product.adjustStock(request.delta());
		
		if (product.getStock() < product.getMinStock()) {
			String message = String.format("""
				Stock bajo para Producto 
				Nombre: %s 
				ID: %s
				Stock actual: %d
				""", product.getName(), product.getId().toString(), product.getStock());
			System.out.println(message);
			try {
				alertSender.sendAlert(emailRecipient, "Alerta de Stock bajo", message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Product updated = productRepository.save(product);
		StockMovement movement = new StockMovement(updated, request.delta(), request.reason());
		
		log.info("Stock del producto actualizado -> Producto: {}, SKU: {}, Cantidad:{} ", updated.getName(), updated.getSku(), movement.getDelta());

		stockMovementRepo.save(movement);
		
		return mapper.toDto(updated);
	}
	
	// Obtener producto por ID
	@Transactional(readOnly= true)
	public ProductResponse getProductById(UUID id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
		return mapper.toDto(product);
	}
	
	// Obtener producto por sku
	public ProductResponse getProductBySku(String sku) {
		Product product = productRepository.findBySku(sku)
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
	
	// Desactivar producto
	public void deactivateProduct(UUID id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No existe un producto con ese id"));
		
		product.setActive(false);
		productRepository.save(product);
		log.info("Producto {} con ID {} se ha desactivado", product.getName(), product.getSku());
	}
	
	// Eliminar producto
	public void deleteProduct(UUID id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No existe un producto con ese id"));
		
		productRepository.deleteById(product.getId());
		log.info("Producto {} con ID {} se ha eliminado", product.getName(), product.getSku());
	}

	
	// Buscar por nombre
	@Transactional(readOnly=true)
	public List<ProductResponse> searchByName(String name) {
		return productRepository.findByNameContainingIgnoreCase(name).stream()
				.map(mapper::toDto)
				.collect(Collectors.toList());
	}
	
	// Filtrar por stock bajo
	@Transactional(readOnly=true)
	public List<ProductResponse> getProductsLowStock(int threshold) {
		return productRepository.findByStockLessThan(threshold).stream()
				.map(mapper::toDto)
				.collect(Collectors.toList());
	}
	
	// Filtrar por rango de precio
	public List<ProductResponse> getProductsByPriceRange(Double min, Double max) {
		return productRepository.findByPriceBetween(min, max).stream()
				.map(mapper::toDto)
				.collect(Collectors.toList());
	}
}
