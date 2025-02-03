package com.ddelight.ddAPI.stripe.services;

import com.ddelight.ddAPI.common.entities.Address;
import com.ddelight.ddAPI.common.entities.Cart;
import com.ddelight.ddAPI.common.entities.Invoice;
import com.ddelight.ddAPI.common.entities.Order;
import com.ddelight.ddAPI.common.exception.NoSuchEntityException;
import com.ddelight.ddAPI.common.repositories.AddressRepo;
import com.ddelight.ddAPI.common.repositories.CartRepo;
import com.ddelight.ddAPI.common.repositories.InvoiceRepo;
import com.ddelight.ddAPI.common.repositories.OrderRepo;
import com.ddelight.ddAPI.common.utils.AuthenticatedUserUtil;
import com.ddelight.ddAPI.firestore.service.FirestoreSyncService;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.RequestOptions;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class StripeService {

    @Value("${secret_key}")
    private String secret_key;

    @Value("${webh_sec}")
    private String endpointSecret;

    private AuthenticatedUserUtil authenticatedUserUtil;

    private final CartRepo cartRepo;

    private final OrderRepo orderRepo;
    private FirestoreSyncService firestoreSyncService;

    private final InvoiceRepo invoiceRepo;

    public StripeService(CartRepo cartRepo,
                         OrderRepo orderRepo,
                         InvoiceRepo invoiceRepo,
                         FirestoreSyncService firestoreSyncService,
                         AuthenticatedUserUtil authenticatedUserUtil) {
        this.cartRepo = cartRepo;
        this.orderRepo = orderRepo;
        this.invoiceRepo = invoiceRepo;
        this.firestoreSyncService = firestoreSyncService;
        this.authenticatedUserUtil = authenticatedUserUtil;
    }

    public String createClientSecret(Double amount) {
        Stripe.apiKey = secret_key;
        System.out.println("email: "+authenticatedUserUtil.getAuthenticatedUser());
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amount.longValue())
                        .setCurrency("lkr")
                        .putMetadata("customer_email",authenticatedUserUtil.getAuthenticatedUser())
                        .addPaymentMethodType("card")
                        .build();

        RequestOptions requestOptions = RequestOptions.builder()
                .setStripeVersionOverride("2022-08-01")
                .build();

        PaymentIntent paymentIntent = null;
        try {
            paymentIntent = PaymentIntent.create(params,requestOptions);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
        return paymentIntent.getClientSecret();
    }

    public void listenAndUpdate(String body,String header){

        Event event= null;

        try {
            event = Webhook.constructEvent(
                    body, header, endpointSecret
            );
        } catch (JsonSyntaxException e) {
            // Invalid payload
            System.out.println(e.getMessage());
        } catch (SignatureVerificationException e) {
            // Invalid signature
            System.out.println(e.getMessage());
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            System.out.println("Deserialization error occurred");
        }

        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                String email = paymentIntent.getMetadata().get("customer_email");
                Long amount = paymentIntent.getAmount();
                addToInvoice(email,amount.doubleValue());
                break;
            case "payment_method.attached":
                PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());

        }
    }

    @Transactional
    public void addToInvoice(String email, Double amount){
        Invoice invoice = new Invoice();
        invoice.setPaidPrice(amount/100);
        invoice.setIssueDate(LocalDateTime.now());
        invoiceRepo.save(invoice);

        List<Cart> list = addOrders(email,invoice);
        firestoreSyncService.syncData(
                authenticatedUserUtil.getAuthenticatedUser(),
                invoice.getInvoiceNumber(),
                firestoreSyncService.getData(list)
        );

    }

    public List<Cart> addOrders(String email,Invoice invoice){
        List<Cart> list = cartRepo.findByUserEmail(email);
        for(Cart cart: list){
            Order order = new Order();
            order.setProduct(cart.getProduct());
            order.setSellPrice(cart.getProduct().getPrice());
            order.setOrderDate(LocalDateTime.now());
            order.setUser(cart.getUser());
            order.setQuantity(cart.getQuantity());
            order.setInvoice(invoice);
            orderRepo.save(order);
        }
        cartRepo.deleteAllByUserEmail(email);
        return list;
    }

}
