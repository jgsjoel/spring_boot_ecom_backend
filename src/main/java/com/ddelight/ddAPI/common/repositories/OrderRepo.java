package com.ddelight.ddAPI.common.repositories;

import com.ddelight.ddAPI.common.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,Long> {

    List<Order> findAllByOrderByIdDesc();

}
