package com.ddelight.ddAPI.firestore.dao;

import java.util.HashMap;
import java.util.Map;

public class OrdersMapper {

    public static void mapper(){
        Map<String,Object> map = new HashMap<>();
        map.put("invoice_id",1);
        map.put("product",1);
        map.put("quantity",1);
        map.put("price",1);
        map.put("date_time",1);
        map.put("status",1);
    }

}
