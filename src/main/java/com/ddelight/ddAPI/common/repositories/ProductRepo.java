package com.ddelight.ddAPI.common.repositories;

import com.ddelight.ddAPI.common.entities.Category;
import com.ddelight.ddAPI.common.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product,Long> {
    Optional<Product> findByName(String name);

    List<Product> findTop5ByOrderByCreatedDesc();

    List<Product> findByDiscountNot(double discount);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByCategory(Category category, Pageable pageable);

    List<Product> findTop5ByCategoryIdAndIdNot(Long categoryId, Long productId);

    Page<Product> findByNameContainingIgnoreCaseAndCategory(String name, Category category, Pageable pageable);
}
