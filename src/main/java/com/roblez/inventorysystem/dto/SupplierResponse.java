package com.roblez.inventorysystem.dto;

import java.util.Set;
import java.util.UUID;

import com.roblez.inventorysystem.domain.PurchaseOrder;

public record SupplierResponse(
		UUID id,
		String fullName,
		String contactEmail,
		String phoneNumber,
		Set<PurchaseOrder> purchaseOrders
) {}
