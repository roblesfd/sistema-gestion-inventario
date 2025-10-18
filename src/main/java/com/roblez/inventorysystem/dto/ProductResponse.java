package com.roblez.inventorysystem.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductResponse(
    UUID id,
    String name,
    String sku,
    String description,
    BigDecimal price,
    Integer stock,
    UUID categoryId,
    String categoryName,
    Boolean active,
    Instant createdAt,
    Instant updatedAt,
    Long version
) {}
