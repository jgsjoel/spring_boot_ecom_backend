package com.ddelight.ddAPI.common.repositories;

import com.ddelight.ddAPI.common.entities.Category;
import com.ddelight.ddAPI.common.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category,Long> {
    Optional<Category> findCategoryByName(String categoryName);

    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
