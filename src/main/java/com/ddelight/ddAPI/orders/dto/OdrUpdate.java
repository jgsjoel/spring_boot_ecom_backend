package com.ddelight.ddAPI.orders.dto;

public record OdrUpdate(
        Long customer_id,
        Long order_id,
        String status
) {
}
