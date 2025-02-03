package com.ddelight.ddAPI.common.repositories;

import com.ddelight.ddAPI.common.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepo extends JpaRepository<Delivery,Long> {

    @Override
    Optional<Delivery> findById(Long districtId);
}
