package com.ddelight.ddAPI.account.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddressRequest(
        @NotEmpty(message = "Please enter Address")
        String address,
        @NotNull(message = "Please select a district")
        Long district
) {
}
