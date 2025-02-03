package com.ddelight.ddAPI.stripe.controllers;

import com.ddelight.ddAPI.stripe.services.StripeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stripe")
public class StripeController {

    private StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @GetMapping("/test")
    public String test() {
        stripeService.addToInvoice("joel@gmail.com",20000.00);
        return "ok";
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            stripeService.listenAndUpdate(payload, sigHeader);
            return ResponseEntity.ok("Webhook received and processed");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
