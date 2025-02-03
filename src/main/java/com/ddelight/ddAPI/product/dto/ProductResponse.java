package com.ddelight.ddAPI.product.dto;

import com.ddelight.ddAPI.common.groups.Update;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Integer quantity,
        Double price,
        Long categoryId,
        Double discount,
        Double weight,
        Boolean active,
        String imageName
) {
}
