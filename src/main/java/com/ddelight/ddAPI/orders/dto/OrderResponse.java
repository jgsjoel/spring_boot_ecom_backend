package com.ddelight.ddAPI.orders.dto;

import java.time.LocalDateTime;

public record OrderResponse(
        Long orderId,
        String invoiceId,
        Double totalPrice
) {
}
