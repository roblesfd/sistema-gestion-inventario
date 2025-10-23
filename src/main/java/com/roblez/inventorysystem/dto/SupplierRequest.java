package com.roblez.inventorysystem.dto;

public record SupplierRequest(
		String fullName,
		String contactEmail,
		String phoneNumber
) {}
