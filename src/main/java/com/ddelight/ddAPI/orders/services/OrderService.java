package com.ddelight.ddAPI.orders.services;

import com.ddelight.ddAPI.common.entities.Order;
import com.ddelight.ddAPI.common.repositories.CartRepo;
import com.ddelight.ddAPI.common.repositories.OrderRepo;
import com.ddelight.ddAPI.common.utils.AuthenticatedUserUtil;
import com.ddelight.ddAPI.orders.dto.OrderResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepo orderRepo;

    private final AuthenticatedUserUtil authenticatedUserUtil;

    private final CartRepo cartRepo;

    public OrderService(OrderRepo orderRepo,
                        AuthenticatedUserUtil authenticatedUserUtil,
                        CartRepo cartRepo) {
        this.orderRepo = orderRepo;
        this.authenticatedUserUtil = authenticatedUserUtil;
        this.cartRepo = cartRepo;
    }

    public List<OrderResponse> getUserOrders(){
        List<OrderResponse> list = new ArrayList<>();
        for(Order order : orderRepo.findAllByOrderByIdDesc()){
            list.add(new OrderResponse(
                    order.getId(),
                    order.getOrderUid(),
                    order.getInvoice().getPaidPrice()
            ));
        }
        return list;
    }

    @Transactional
    public void deleteAllItemsByUser() {
        cartRepo.deleteAllByUserEmail(authenticatedUserUtil.getAuthenticatedUser());
    }



}
