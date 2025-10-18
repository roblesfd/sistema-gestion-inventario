package com.roblez.inventorysystem.dto;

import java.time.Instant;
import java.util.UUID;

public record StockMovementResponse(
    UUID id,
    UUID productId,
    Integer delta,
    Instant happenedAt,
    String reason
) {}