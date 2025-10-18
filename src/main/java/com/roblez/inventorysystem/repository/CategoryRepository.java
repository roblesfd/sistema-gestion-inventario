package com.roblez.inventorysystem.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roblez.inventorysystem.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID>{
	Optional<Category> findById(UUID id);	
	Optional<Category> findByName(String name);	
	boolean existsByName(String name);
	List<Category> findByNameContainingIgnoreCase(String name);
	List<Category> findByActiveTrue();
}
