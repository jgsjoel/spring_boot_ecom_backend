package com.ddelight.ddAPI.account.dto;

public record UserResponse(
        String firstName,
        String lastName,
        String email,
        String mobile
) {
}
