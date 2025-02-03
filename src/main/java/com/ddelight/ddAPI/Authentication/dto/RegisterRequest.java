package com.ddelight.ddAPI.Authentication.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotEmpty(message = "Please enter first name")
        String firstName,
        @NotEmpty(message = "Please enter last name")
        String lastName,
        @Email(message = "Invalid Email")
        @NotEmpty(message = "Please enter email")
        String email,
        @NotEmpty(message = "Please enter password")
        String password1,
        @NotEmpty(message = "Please enter confirm password")
        String password2
) {
}
