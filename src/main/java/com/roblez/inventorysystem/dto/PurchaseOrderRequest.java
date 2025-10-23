package com.roblez.inventorysystem.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.roblez.inventorysystem.domain.OrderStatus;

public record PurchaseOrderRequest(
	OrderStatus status,
	UUID supplierId,
	UUID generatedById,
	Instant expectedDeliveryDate,
	Set<PurchaseOrderItemRequest> items
){}
