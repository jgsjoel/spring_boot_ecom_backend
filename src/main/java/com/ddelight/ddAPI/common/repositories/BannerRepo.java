package com.ddelight.ddAPI.common.repositories;

import com.ddelight.ddAPI.common.entities.Banner;
import com.ddelight.ddAPI.common.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepo extends JpaRepository<Banner,Long> {
    Page<Banner> findByTitleContainingIgnoreCase(String name, Pageable pageable);
}
