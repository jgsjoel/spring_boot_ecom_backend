package com.ddelight.ddAPI.category.controllers;

import com.ddelight.ddAPI.category.dtos.CategoryResponse;
import com.ddelight.ddAPI.category.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/category")
public class ClientCategoryController {

    private CategoryService categoryService;

    public ClientCategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> getCategories(){
        return new ResponseEntity<>(categoryService.getClientCategories(), HttpStatus.OK);
    }

}
