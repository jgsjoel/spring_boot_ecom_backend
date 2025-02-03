package com.ddelight.ddAPI.cart.controllers;

import com.ddelight.ddAPI.cart.dto.CartRequest;
import com.ddelight.ddAPI.cart.services.CartService;
import com.ddelight.ddAPI.common.groups.Create;
import com.ddelight.ddAPI.common.groups.Delete;
import com.ddelight.ddAPI.common.groups.Update;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<?> getCartItems() {
        return new ResponseEntity<>(cartService.getAllCartItemsByUser(),HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCartItemsCount() {
        return new ResponseEntity<>(cartService.getItemCount(),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addItemToCart(@Validated(Create.class) @RequestBody CartRequest cartRequest) {
        cartService.addToCart(cartRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateCart(@Validated(Update.class) @RequestBody CartRequest cartRequest) {
        cartService.updateItemQuantity(cartRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> removeItemFromCart(@Validated(Delete.class) @RequestBody CartRequest cartRequest) {
        cartService.deleteSingleItem(cartRequest.productId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
