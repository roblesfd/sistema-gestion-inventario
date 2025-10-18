package com.roblez.inventorysystem.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateStockMovementRequest(
    @NotNull Integer delta,
    String reason
) {}
