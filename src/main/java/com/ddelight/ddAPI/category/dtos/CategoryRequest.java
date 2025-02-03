package com.ddelight.ddAPI.category.dtos;

import com.ddelight.ddAPI.common.groups.Create;
import com.ddelight.ddAPI.common.groups.Delete;
import com.ddelight.ddAPI.common.groups.Read;
import com.ddelight.ddAPI.common.groups.Update;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CategoryRequest(
        @NotNull(message = "Invalid Category Selected",groups = {Update.class, Delete.class})
        Long id,
        @NotEmpty(message = "Please enter category name",groups = {Create.class, Update.class, Read.class})
        String categoryName,
        @NotNull(message = "Please select an image",groups = {Create.class})
        MultipartFile file
) {
}
