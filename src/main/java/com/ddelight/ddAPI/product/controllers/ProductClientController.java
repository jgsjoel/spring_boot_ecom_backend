package com.ddelight.ddAPI.product.controllers;

import com.ddelight.ddAPI.common.entities.Product;
import com.ddelight.ddAPI.product.dto.ProductResponse;
import com.ddelight.ddAPI.product.services.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
public class ProductClientController {

    private ProductService productService;
    private final int PAGE_SIZE = 8;

    public ProductClientController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> getProducts(@RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "categoryId", required = false) Long categoryId,
                                         @RequestParam(value = "pageNumber",required = false,defaultValue = "0") int pageNumber,
                                         @RequestParam(value = "productId",required = false) Long productId){
        if(name != null && !name.isEmpty()){
            return new ResponseEntity<>(getProductsByName(name,pageNumber),HttpStatus.OK);
        } else if (categoryId != null) {
            return new ResponseEntity<>(getProductsByCategoryId(categoryId,pageNumber),HttpStatus.OK);
        } else if (name == null && categoryId == null && productId == null) {
            return new ResponseEntity<>(getProducts(),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id){
        return new ResponseEntity<>(productService.getSingleProductById(id),HttpStatus.OK);
    }

    @GetMapping("/discounts")
    public ResponseEntity<?> getDiscountedProducts(){
        return new ResponseEntity<Map<String,Object>>(productService.loadDiscountedItems(),HttpStatus.OK);
    }

    public Map<String ,Object> getProductsByCategoryId(Long categoryId, int pageNumber){
         return productService.findByCategoryId(categoryId,pageNumber,PAGE_SIZE);
    }

    public Map<String ,Object> getProductsByName(String name,int pageNumber){
        return productService.findByName(name,pageNumber,PAGE_SIZE);
    }

    public Map<String ,Object> getProducts(){
        return productService.loadClientHomeProducts();
    }


    @GetMapping("/related")
    public ResponseEntity<?> getRelatedItems(
            @RequestParam(value = "categoryId") Long categoryId,
            @RequestParam(value = "productId") Long productId
    ){
        return new ResponseEntity<>(productService.findRelatedProducts(categoryId,productId),HttpStatus.OK);
    }

}
