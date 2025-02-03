package com.ddelight.ddAPI.cart.dto;

import com.ddelight.ddAPI.common.groups.Create;
import com.ddelight.ddAPI.common.groups.Delete;
import com.ddelight.ddAPI.common.groups.Update;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CartRequest(
        @NotNull(message = "Invalid item selected",groups = {Create.class, Update.class, Delete.class})
        Long productId,
        @NotNull(message = "Please enter a valid quantity",groups = {Create.class, Update.class})
        Integer quantity
) {
}
