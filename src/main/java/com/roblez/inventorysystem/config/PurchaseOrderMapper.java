package com.roblez.inventorysystem.config;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.roblez.inventorysystem.domain.PurchaseOrder;
import com.roblez.inventorysystem.dto.PurchaseOrderRequest;
import com.roblez.inventorysystem.dto.PurchaseOrderResponse;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {
	PurchaseOrderMapper INSTANCE = Mappers.getMapper(PurchaseOrderMapper.class);
	
	PurchaseOrder toEntity(PurchaseOrderRequest request);
	PurchaseOrderResponse toDto(PurchaseOrder purchaseOrder);
}
