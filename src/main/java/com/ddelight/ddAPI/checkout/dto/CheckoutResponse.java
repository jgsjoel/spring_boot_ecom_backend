package com.ddelight.ddAPI.checkout.dto;

public record CheckoutResponse(
        Double subTotal,
        String userName,
        String mobile,
        String address,
        Float desLat,
        Float desLon,
        Float srcLat,
        Float srcLon,
        Double basePrice,
        Double incPricce,
        Integer baseRadius
) {
}
