package com.ddelight.ddAPI.common.repositories;

import com.ddelight.ddAPI.common.entities.Cart;
import com.ddelight.ddAPI.common.entities.Product;
import com.ddelight.ddAPI.common.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart,Long> {

    List<Cart> findByUserEmail(String email);

    Optional<Cart> findByUserEmailAndProduct(String email, Product product);

    @Transactional
    void deleteAllByUserEmail(String email);

}
