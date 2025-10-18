package com.roblez.inventorysystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequest(
    @NotBlank String name,
    @NotBlank String sku,
    String description,
    @NotNull @Min(0) BigDecimal price,
    @NotNull @Min(0) Integer stock,
    UUID categoryId,          // opcional: resolver en el service
    String categoryName,      // opcional: alternativa a categoryId
    Boolean active            // opcional, puede ser null para dejar sin cambio
) {}
