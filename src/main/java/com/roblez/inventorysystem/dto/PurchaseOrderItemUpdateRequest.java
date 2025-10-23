package com.roblez.inventorysystem.dto;

import java.util.UUID;

public record PurchaseOrderItemUpdateRequest(
	UUID id,
	UUID purchaseOrderId,
	UUID productId,
	int quantity,
	double unitPrice
){}
	