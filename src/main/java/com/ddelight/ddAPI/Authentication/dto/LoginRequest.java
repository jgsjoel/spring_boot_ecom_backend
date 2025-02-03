package com.ddelight.ddAPI.Authentication.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty(message = "Enter Email")
        String email,
        @NotEmpty(message = "Enter Password")
        String password
){}
