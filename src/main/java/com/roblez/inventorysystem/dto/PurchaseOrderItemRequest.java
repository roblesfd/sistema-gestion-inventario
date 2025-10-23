package com.roblez.inventorysystem.dto;

import java.util.UUID;

public record PurchaseOrderItemRequest(
	UUID productId,
	int quantity,
	double unitPrice
){}
	