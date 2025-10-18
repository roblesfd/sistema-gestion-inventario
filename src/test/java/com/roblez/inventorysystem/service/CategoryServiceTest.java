package com.roblez.inventorysystem.service;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.roblez.inventorysystem.config.CategoryMapper;
import com.roblez.inventorysystem.domain.Category;
import com.roblez.inventorysystem.dto.CategoryRequest;
import com.roblez.inventorysystem.dto.CategoryResponse;
import com.roblez.inventorysystem.repository.CategoryRepository;

class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;
    
	@Mock
	private CategoryMapper mapper;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCategory_ShouldThrow_WhenNameExists() {
        when(categoryRepository.existsByName("Electronics")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> categoryService.createCategory(new CategoryRequest("Electronics", true)));
        assertEquals("Ya existe una categoría con ese nombre", ex.getMessage());
    }

    @Test
    void createCategory_ShouldSucceed_WhenNameUnique() {
        when(categoryRepository.existsByName("Toys")).thenReturn(false);
        Category category = new Category("Toys", true);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        
        when(mapper.toDto(any(Category.class)))
        .thenAnswer(inv -> new CategoryResponse(((Category)inv.getArgument(0)).getId(),
                                               ((Category)inv.getArgument(0)).getName(),
                                               ((Category)inv.getArgument(0)).getActive()));

        CategoryResponse result = categoryService.createCategory(new CategoryRequest("Toys", true));

        assertNotNull(result);
        assertEquals("Toys", result.name());
        assertTrue(result.active());
    }
}