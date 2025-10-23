package com.roblez.inventorysystem.dto;

import java.util.UUID;

public record PurchaseOrderItemResponse(
	UUID id,
	ProductResponse product,
	int quantity,
	double unitPrice
){}
	