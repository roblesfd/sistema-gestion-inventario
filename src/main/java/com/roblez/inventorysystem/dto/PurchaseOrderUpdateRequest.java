package com.roblez.inventorysystem.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.roblez.inventorysystem.domain.OrderStatus;

public record PurchaseOrderUpdateRequest(
	UUID id,
	OrderStatus status,
	UUID supplier,
	UUID generatedBy,
	Instant expectedDeliveryDate,
	Set<PurchaseOrderItemUpdateRequest> items,
	double total
){}
