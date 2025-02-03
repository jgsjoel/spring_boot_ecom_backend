package com.ddelight.ddAPI.firestore.service;

import com.ddelight.ddAPI.common.entities.Cart;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FirestoreSyncService {

    //collection = orders
        //document = invoice id
            //active = true/false
            //status = "Status label"
            //email = "user email goes here"
            //productList = Map<String,name>
                        //= Map<number,id>
                        //= Map<number,price>
                        //= Map<number,qty>

    private final Firestore firestore = FirestoreClient.getFirestore();

    public void syncData(String userEmail, String invoiceId, List<Map<String, Object>> data) {

        try {
            DocumentReference docRef = firestore.collection("Orders").document(invoiceId);

            Map<String, Object> fields = new HashMap<>();
            fields.put("active", true);
            fields.put("status", "ORDER RECEIVED");
            fields.put("email", userEmail);
            fields.put("products", data);
            docRef.set(fields);

            docRef.set(fields);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Map<String, Object>> getData(List<Cart> list) {
        List<Map<String, Object>> data = new ArrayList<>();
        for(Cart cart : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("name",cart.getProduct().getName());
            map.put("product_id",cart.getProduct().getId());
            map.put("quantity",cart.getQuantity());
            map.put("price",cart.getProduct().getPrice());
            data.add(map);
        }
        return data;
    }




}
