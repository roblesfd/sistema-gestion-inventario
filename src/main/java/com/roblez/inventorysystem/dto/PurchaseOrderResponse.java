package com.roblez.inventorysystem.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.roblez.inventorysystem.domain.OrderStatus;

public record PurchaseOrderResponse(
	UUID id,
	Instant createdAt,
	OrderStatus status,
	SupplierResponse supplier,
	UserRequest generatedBy,
	Instant expectedDeliveryDate,
	Set<PurchaseOrderItemResponse> items,
	double total
	)
{}
