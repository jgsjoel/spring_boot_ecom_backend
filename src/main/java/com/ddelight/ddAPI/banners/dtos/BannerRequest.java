package com.ddelight.ddAPI.banners.dtos;

import com.ddelight.ddAPI.common.groups.Create;
import com.ddelight.ddAPI.common.groups.Delete;
import com.ddelight.ddAPI.common.groups.Update;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record BannerRequest(
        @NotNull(message = "Please select a banner",groups = {Update.class, Delete.class})
        Long id,
        @NotEmpty(message = "Please enter banner title",groups = {Update.class, Create.class})
        String title,
        @NotNull(message = "Please select an image",groups = {Create.class})
        MultipartFile file
) {
}