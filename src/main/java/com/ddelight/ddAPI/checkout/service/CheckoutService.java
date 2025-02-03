package com.ddelight.ddAPI.checkout.service;

import com.ddelight.ddAPI.cart.services.CartService;
import com.ddelight.ddAPI.checkout.dto.CheckoutRequest;
import com.ddelight.ddAPI.checkout.dto.CheckoutResponse;
import com.ddelight.ddAPI.common.entities.Address;
import com.ddelight.ddAPI.common.entities.Cart;
import com.ddelight.ddAPI.common.entities.Delivery;
import com.ddelight.ddAPI.common.entities.User;
import com.ddelight.ddAPI.common.exception.NoSuchEntityException;
import com.ddelight.ddAPI.common.repositories.*;
import com.ddelight.ddAPI.common.utils.AuthenticatedUserUtil;
import com.ddelight.ddAPI.stripe.services.StripeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CheckoutService {

    private final CartService cartService;
    private AuthenticatedUserUtil authenticatedUserUtil;
    private CartRepo cartRepo;
    private AddressRepo addressRepo;
    private DeliveryRepo deliveryRepo;
    private StripeService stripeService;

    public CheckoutService(
            AuthenticatedUserUtil authenticatedUserUtil,
            CartRepo cartRepo,
            DeliveryRepo deliveryRepo,
            StripeService stripeService,
            AddressRepo addressRepo,
            CartService cartService
    ) {
        this.authenticatedUserUtil = authenticatedUserUtil;
        this.cartRepo = cartRepo;
        this.addressRepo = addressRepo;
        this.cartService = cartService;
        this.stripeService = stripeService;
        this.deliveryRepo = deliveryRepo;
    }

    public double loadSubTotal() {
        List<Cart> list = cartRepo.findByUserEmail(authenticatedUserUtil.getAuthenticatedUser());
        double subtTotal = 0;
        for (Cart obj : list) {
            double discount = obj.getProduct().getDiscount();
            double price = obj.getProduct().getPrice();
            double quantity = obj.getQuantity();
            subtTotal = subtTotal + (price - (price * discount / 100)) * quantity;
        }
        return subtTotal;
    }

    private Delivery getDelivery(){
        Optional<Delivery> obj = deliveryRepo.findById(1L);
        if(!obj.isPresent()){
            throw new NoSuchEntityException("Delivery Details Fetch Failed");
        }
        return obj.get();
    }

    public Address getAddress() {
        Optional<Address> address = addressRepo.findByUserEmail(authenticatedUserUtil.getAuthenticatedUser());
        if (address.isPresent()) {
            return address.get();
        }
        return null;
    }

    public String saveUserAddress(CheckoutRequest request) {
        Address address = getAddress();
        if(address == null){
            address = new Address();
            address.setUser(cartService.getUser());
        }
        address.setLatitude(request.latitude());
        address.setLongitude(request.longitude());
        address.setAddress(request.address());
        addressRepo.save(address);

        return stripeService.createClientSecret(request.total()*100);

    }

    public CheckoutResponse checkoutDetails() {
        Address address = getAddress();
        User user = cartService.getUser();
        Delivery delivery = getDelivery();
        return new CheckoutResponse(
                loadSubTotal(),
                user.getFirstName()+" "+user.getLaseName(),
                user.getMobile(),
                (address != null)?address.getAddress():null,
                (address != null)?address.getLatitude():null,
                (address != null)?address.getLongitude():null,
                delivery.getLatitude(),
                delivery.getLongitude(),
                delivery.getBase_cost(),
                delivery.getIncrement_cost(),
                delivery.getBase_radius()
        );
    }

}
