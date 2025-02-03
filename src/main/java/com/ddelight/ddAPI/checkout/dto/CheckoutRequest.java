package com.ddelight.ddAPI.checkout.dto;

import jakarta.validation.constraints.NotEmpty;

public record CheckoutRequest(
        String address,
        Float latitude,
        Float longitude,
        Double total
) {
}
