package com.ddelight.ddAPI.category.dtos;

import jakarta.annotation.Nullable;

public record CategoryResponse(
        Long id,
        String categoryName,
        String imageUrl
) { }
