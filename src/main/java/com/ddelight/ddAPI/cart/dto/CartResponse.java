package com.ddelight.ddAPI.cart.dto;

public record CartResponse(
        Long productId,
        String productName,
        Double productPrice,
        Integer quantity,
        Double discount,
        String productImage,
        Integer selectedQuantity
) {
}
