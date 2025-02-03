package com.ddelight.ddAPI.orders.coltrollers;

import com.ddelight.ddAPI.orders.dto.OdrUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderClientController {

    @GetMapping
    public ResponseEntity<?> getOrders(){
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/update")
    @SendTo("/topic/listen")
    public String handleRequest(@RequestBody OdrUpdate request){
        System.out.println(request.status());
        return request.status();
    }



}
