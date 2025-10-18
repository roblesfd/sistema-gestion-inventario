package com.roblez.inventorysystem.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roblez.inventorysystem.dto.CategoryRequest;
import com.roblez.inventorysystem.dto.CategoryResponse;
import com.roblez.inventorysystem.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	private final CategoryService categoryService;
	
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    @Operation(summary = "Crea una categoría", description = "Agrega una nueva categoría al inventario")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse dto = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse dto = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
    	List<CategoryResponse> list = categoryService.getAllCategories();
    	
    	return ResponseEntity.ok(list);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponse>> getActiveCategories() {
    	List<CategoryResponse> list = categoryService.getActiveCategories();
    	return ResponseEntity.ok(list);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable UUID id) {
    	CategoryResponse category = categoryService.getCategoryById(id);
    	
    	return ResponseEntity.ok(category);
    }
    
    @Operation(summary="Desactivar categoría", description="Cambia el valor de campo active a false")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateCategory(@PathVariable UUID id) {
    	categoryService.deactiveCategory(id);
    	
    	return ResponseEntity.noContent().build();
    }
}
