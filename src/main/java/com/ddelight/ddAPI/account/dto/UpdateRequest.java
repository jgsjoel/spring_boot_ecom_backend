package com.ddelight.ddAPI.account.dto;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public record UpdateRequest(
        @NotEmpty(message = "Please enter first name")
        String firstName,
        @NotEmpty(message = "Please enter last name")
        String lastName,
        @NotEmpty(message = "Please enter mobile")
        String mobile
) {
}
