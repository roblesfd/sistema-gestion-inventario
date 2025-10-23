package com.roblez.inventorysystem.config;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.roblez.inventorysystem.domain.PurchaseOrderItem;
import com.roblez.inventorysystem.dto.PurchaseOrderItemResponse;
import com.roblez.inventorysystem.dto.PurchaseOrderItemUpdateRequest;

@Mapper(componentModel = "spring")
public interface PurchaseOrderItemMapper {
	PurchaseOrderItemMapper INSTANCE = Mappers.getMapper(PurchaseOrderItemMapper.class);
	

    @Mapping(target = "purchaseOrder", ignore = true)
    @Mapping(target = "id") // mapea id si viene (útil en pruebas); pero para nuevos puede ser null
    PurchaseOrderItem toEntity(PurchaseOrderItemUpdateRequest request);
    
    // Actualiza una entidad existente desde el request (no reemplaza la entidad completa)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "purchaseOrder", ignore = true)
    void updateFromRequest(PurchaseOrderItemUpdateRequest request, @MappingTarget PurchaseOrderItem entity);

    
	PurchaseOrderItemResponse toDto(PurchaseOrderItem purchaseOrderItem);
}
