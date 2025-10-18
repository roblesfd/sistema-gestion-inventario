package com.roblez.inventorysystem.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.roblez.inventorysystem.domain.Product;
import com.roblez.inventorysystem.dto.ProductRequest;
import com.roblez.inventorysystem.dto.ProductResponse;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
	
	@Mapping(target="category", ignore= true)
	Product toEntity(ProductRequest dto);
	
	@Mapping(source= "category.name", target= "categoryName")
	ProductResponse toDto(Product product);

}
