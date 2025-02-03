package com.ddelight.ddAPI.checkout.coltrollers;

import com.ddelight.ddAPI.checkout.dto.CheckoutRequest;
import com.ddelight.ddAPI.checkout.dto.CheckoutResponse;
import com.ddelight.ddAPI.checkout.service.CheckoutService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    private CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @GetMapping
    public ResponseEntity<CheckoutResponse> checkout() {
        return new ResponseEntity<CheckoutResponse>(checkoutService.checkoutDetails(),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> saveOrder(@Valid @RequestBody CheckoutRequest request){
        System.out.println("post method");
        return new ResponseEntity<String>(checkoutService.saveUserAddress(request),HttpStatus.OK);
    }

}
