package com.roblez.inventorysystem.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.roblez.inventorysystem.domain.StockMovement;
import com.roblez.inventorysystem.dto.StockMovementResponse;

@Mapper(componentModel= "spring")
public interface StockMovementMapper {
	StockMovementMapper INSTANCE = Mappers.getMapper(StockMovementMapper.class);
	
	@Mapping(source= "product.id", target="productId")
	StockMovementResponse toDto(StockMovement entity);
}
