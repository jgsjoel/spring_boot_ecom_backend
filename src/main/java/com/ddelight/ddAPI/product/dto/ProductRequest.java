package com.ddelight.ddAPI.product.dto;

import com.ddelight.ddAPI.common.groups.Create;
import com.ddelight.ddAPI.common.groups.Delete;
import com.ddelight.ddAPI.common.groups.Update;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record ProductRequest(

        @NotNull(message = "Invalid Item Selected",groups = {Update.class, Delete.class})
        Long id,
        @NotEmpty(message = "Please enter product name",groups = {Update.class,Create.class})
        String name,
        @NotEmpty(message = "Please enter product description",groups = {Update.class,Create.class})
        String description,
        @NotNull(message = "Please enter product quantity",groups = {Update.class,Create.class})
        Integer quantity,
        @NotNull(message = "Please enter price",groups = {Update.class,Create.class})
        Double price,
        @NotNull(message = "Please select category",groups = {Update.class,Create.class})
        Long categoryId,
        @NotNull(message = "Please select Discount",groups = {Update.class,Create.class})
        Double discount,
        @NotNull(message = "Please select item weight",groups = {Update.class,Create.class})
        Double weight,
        @NotNull(message="Please select an image",groups = {Create.class})
        MultipartFile file
) { }
