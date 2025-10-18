package com.roblez.inventorysystem.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.roblez.inventorysystem.config.CategoryMapper;
import com.roblez.inventorysystem.domain.Category;
import com.roblez.inventorysystem.dto.CategoryRequest;
import com.roblez.inventorysystem.dto.CategoryResponse;
import com.roblez.inventorysystem.repository.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {
	private final CategoryRepository categoryRepo;
	private final CategoryMapper mapper;

	public CategoryService(CategoryRepository categoryRepo,  CategoryMapper mapper) {
		this.categoryRepo = categoryRepo;
		this.mapper = mapper;
	}
	
	public CategoryResponse createCategory(CategoryRequest request) {
		if(categoryRepo.existsByName(request.name())) {
			throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
		}
		
		Category category = new Category();
		category.setName(request.name());
		category.setActive(request.active() != null ? request.active() : true);
		Category saved = categoryRepo.save(category);
		return mapper.toDto(saved);
	}
	
	public CategoryResponse updateCategory(UUID id, CategoryRequest request) {
		Category category = categoryRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("No se encontro la categor"));
		
        category.setName(request.name());
        if (request.active() != null) category.setActive(request.active());
        
        Category updated = categoryRepo.save(category);
        return mapper.toDto(updated);
	}
	
   public List<CategoryResponse> getAllCategories() {
        return categoryRepo.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
   
   public List<CategoryResponse> getActiveCategories() {
       return categoryRepo.findByActiveTrue().stream()
               .map(mapper::toDto)
               .collect(Collectors.toList());
   }
   
   public CategoryResponse getCategoryById(UUID id) {
	   Category category = categoryRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("No existe una categoría con ese nombre")); 
	   return mapper.toDto(category);
   }
   
   public void deactiveCategory(UUID id) {
	   Category category = categoryRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("No existe una categoría con ese nombre")); 
	   
	   category.setActive(false);
	   categoryRepo.save(category);
   }
}
