package com.roblez.inventorysystem.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.roblez.inventorysystem.domain.Supplier;
import com.roblez.inventorysystem.dto.SupplierRequest;
import com.roblez.inventorysystem.dto.SupplierResponse;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

	SupplierMapper INSTANCE = Mappers.getMapper(SupplierMapper.class);
	
	Supplier toEntity(SupplierRequest request);
	SupplierResponse toDto(Supplier supplier);

}
