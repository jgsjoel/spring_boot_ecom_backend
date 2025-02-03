package com.ddelight.ddAPI.product.controllers;

import com.ddelight.ddAPI.common.groups.Create;
import com.ddelight.ddAPI.common.groups.Update;
import com.ddelight.ddAPI.product.dto.ProductRequest;
import com.ddelight.ddAPI.product.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/products")
public class ProductAdminController {

    private ProductService productService;
    private int PAGESIZE = 5;

    public ProductAdminController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "pageNumber",required = false,defaultValue = "0") int pageNumber
            ){
        if(name != null && !name.isEmpty()){
            return new ResponseEntity<>(getProductsByName(name,pageNumber),HttpStatus.OK);
        }else if(id != null){
            return new ResponseEntity<>(productService.getSingleProductById(id),HttpStatus.OK);
        }else if(name == null || name.isEmpty()){
            return new ResponseEntity<>(getAllProducts(pageNumber),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Long id){
        productService.changeStatus(id);
        return new ResponseEntity<String>("Status Changed",HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addNewProduct(@Validated(Create.class) @ModelAttribute ProductRequest dto) throws IOException {
        productService.saveProduct(dto, dto.file());
        return new ResponseEntity<>("Product has been saved",HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(@Validated(Update.class) @ModelAttribute ProductRequest dto) throws IOException {
        productService.updateProduct(dto,dto.file());
        return new ResponseEntity<>("Product Updated!",HttpStatus.OK);
    }

    public Map<String ,Object> getProductsByName(String name, int pageNumber){
        return productService.findByName(name,pageNumber,PAGESIZE);
    }

    public Map<String ,Object> getAllProducts(int pageNumber){
        return productService.loadProducts(pageNumber,PAGESIZE);
    }

}
