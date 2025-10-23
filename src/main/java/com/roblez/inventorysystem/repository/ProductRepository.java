package com.roblez.inventorysystem.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.roblez.inventorysystem.domain.Category;
import com.roblez.inventorysystem.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>{
	List<Product> findAll();
	Optional<Product> findById(UUID id);
	Optional<Product> findBySku(String sku);
	List<Product> findByCategory(Category category);
	List<Product> findByNameContainingIgnoreCase(String name);
	List<Product> findByStockLessThan(Integer stock);
	List<Product> findByActiveTrue();
	List<Product> findAllByOrderByPriceAsc();
	List<Product> findByPriceBetween(Double min, Double max);
	boolean existsBySku(String sku);
	List<Product> findBySkuIn(Set<String> skus);
	List<Product> findByIdIn(Set<UUID> productIds);
}
