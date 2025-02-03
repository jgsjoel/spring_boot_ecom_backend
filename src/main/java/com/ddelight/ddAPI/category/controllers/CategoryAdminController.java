package com.ddelight.ddAPI.category.controllers;

import com.ddelight.ddAPI.category.dtos.CategoryRequest;
import com.ddelight.ddAPI.category.dtos.CategoryResponse;
import com.ddelight.ddAPI.category.services.CategoryService;
import com.ddelight.ddAPI.common.groups.Create;
import com.ddelight.ddAPI.common.groups.Delete;
import com.ddelight.ddAPI.common.groups.Update;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/category")
public class CategoryAdminController {

    private CategoryService categoryService;

    public CategoryAdminController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> getCategories(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "pageNumber",required = false,defaultValue = "0") int pageNumber
    ){
        if(name != null && !name.isEmpty()){
            return new ResponseEntity<>(categoryService.getCategoryByName(name,pageNumber),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(categoryService.getAllCategories(pageNumber), HttpStatus.OK);
        }
    }

    @GetMapping("/ni")
    public ResponseEntity<?> getCategoriesWithoutImage(){
        return new ResponseEntity<List<CategoryResponse>>(categoryService.getAllCategoriesOnly(), HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> saveCategory(@Validated(Create.class) @ModelAttribute CategoryRequest dto) throws IOException {
        categoryService.saveCategory(dto, dto.file());
        return new ResponseEntity<String>("Upload Complete",HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(@Validated(Update.class) @ModelAttribute CategoryRequest dto) throws IOException {
        categoryService.updateCategory(dto,dto.file());
        return new ResponseEntity<>("Update Successful",HttpStatus.OK);

    }

    @DeleteMapping
    public ResponseEntity<?> delete(@Validated(Delete.class) @RequestBody CategoryRequest dto){
        categoryService.delete(dto.id());
        return new ResponseEntity<String>("Delete Successful",HttpStatus.OK);
    }

}
