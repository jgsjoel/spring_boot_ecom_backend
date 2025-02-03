package com.ddelight.ddAPI.cart.services;

import com.ddelight.ddAPI.cart.dto.CartRequest;
import com.ddelight.ddAPI.cart.dto.CartResponse;
import com.ddelight.ddAPI.common.entities.Cart;
import com.ddelight.ddAPI.common.entities.Product;
import com.ddelight.ddAPI.common.entities.User;
import com.ddelight.ddAPI.common.exception.DuplicateEntityException;
import com.ddelight.ddAPI.common.exception.ExceededQuantityException;
import com.ddelight.ddAPI.common.exception.NoSuchEntityException;
import com.ddelight.ddAPI.common.repositories.CartRepo;
import com.ddelight.ddAPI.common.repositories.ProductRepo;
import com.ddelight.ddAPI.common.repositories.UserRepo;
import com.ddelight.ddAPI.common.services.CloudStorageService;
import com.ddelight.ddAPI.common.utils.AuthenticatedUserUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private CartRepo cartRepo;
    private ProductRepo productRepo;
    private UserRepo userRepo;
    private CloudStorageService storageService;
    private AuthenticatedUserUtil authenticatedUserUtil;

    public CartService(CartRepo cartRepo,
                       ProductRepo productRepo,
                       UserRepo userRepo,
                       CloudStorageService storageService,
                       AuthenticatedUserUtil authenticatedUserUtil) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.storageService = storageService;
        this.authenticatedUserUtil = authenticatedUserUtil;
    }

    public User getUser(){
        Optional<User> userOptional = userRepo.findByEmail(authenticatedUserUtil.getAuthenticatedUser());
        if(!userOptional.isPresent()){
            throw new NoSuchEntityException("User not found");
        }
        return userOptional.get();
    }

    private Product getProductById(Long id){
        Optional<Product> product = productRepo.findById(id);
        if (!product.isPresent()){
            throw new NoSuchEntityException("Product not found");
        }
        return product.get();
    }

    private Optional<Cart> getCartItem(Long productId){
        return cartRepo.findByUserEmailAndProduct(authenticatedUserUtil.getAuthenticatedUser(),getProductById(productId));
    }

    public void addToCart(CartRequest request) {

        if(getCartItem(request.productId()).isPresent()){
           throw new DuplicateEntityException("Item Already added to cart");
        }

        Product product = getProductById(request.productId());
        if(product.getAvailableQty()< request.quantity()){
            throw new ExceededQuantityException("Select a less quantity");
        }

        Cart cart = new Cart();
        cart.setQuantity(request.quantity());
        cart.setProduct(product);
        cart.setUser(getUser());
        cartRepo.save(cart);
    }

    public List<CartResponse> getAllCartItemsByUser() {
        List<CartResponse> cartResponses = new ArrayList<>();
        for(Cart cartObj:cartRepo.findByUserEmail(authenticatedUserUtil.getAuthenticatedUser())){
            cartResponses.add(new CartResponse(
                    cartObj.getProduct().getId(),
                    cartObj.getProduct().getName(),
                    cartObj.getProduct().getPrice(),
                    cartObj.getProduct().getAvailableQty(),
                    cartObj.getProduct().getDiscount(),
                    storageService.download(cartObj.getProduct().getImageName()),
                    cartObj.getQuantity()
            ));
        }
        return cartResponses;
    }

    public int getItemCount() {
        return cartRepo.findByUserEmail(authenticatedUserUtil.getAuthenticatedUser()).size();
    }

    @Transactional
    public void updateItemQuantity(CartRequest request){
            Optional<Cart> cartOptional = getCartItem(request.productId());
            if(!cartOptional.isPresent()){
                throw new NoSuchEntityException("Update Failed");
            }
            Cart cart = cartOptional.get();
            cart.setQuantity(request.quantity());
            cartRepo.save(cart);
    }

    @Transactional
    public void deleteSingleItem(Long productId){
        Optional<Cart> cartOptional = getCartItem(productId);
        if(!cartOptional.isPresent()){
            throw new NoSuchEntityException("Delete Failed");
        }
        cartRepo.delete(cartOptional.get());
    }


}
