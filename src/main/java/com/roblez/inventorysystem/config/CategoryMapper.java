package com.roblez.inventorysystem.config;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.roblez.inventorysystem.domain.Category;
import com.roblez.inventorysystem.dto.CategoryRequest;
import com.roblez.inventorysystem.dto.CategoryResponse;

@Mapper(componentModel= "spring")
public interface CategoryMapper {
	CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
	
	Category toEntity(CategoryRequest dto);
	CategoryResponse toDto(Category entity);
}
