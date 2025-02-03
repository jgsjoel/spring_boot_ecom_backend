package com.ddelight.ddAPI.category.services;

import com.ddelight.ddAPI.category.dtos.CategoryRequest;
import com.ddelight.ddAPI.category.dtos.CategoryResponse;
import com.ddelight.ddAPI.common.entities.Category;
import com.ddelight.ddAPI.common.entities.Product;
import com.ddelight.ddAPI.common.enums.UploadType;
import com.ddelight.ddAPI.common.exception.DuplicateEntityException;
import com.ddelight.ddAPI.common.exception.InvalidFileTypeException;
import com.ddelight.ddAPI.common.exception.NoSuchEntityException;
import com.ddelight.ddAPI.common.repositories.CategoryRepo;
import com.ddelight.ddAPI.common.services.CloudStorageService;
import com.ddelight.ddAPI.product.dto.ProductResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CloudStorageService cloudStorageService;
    private CategoryRepo categoryRepo;
    private CloudStorageService storageService;
    private final int PAGE_SIZE = 2;

    public CategoryService(CategoryRepo categoryRepo, CloudStorageService storageService, CloudStorageService cloudStorageService){
        this.categoryRepo = categoryRepo;
        this.storageService = storageService;
        this.cloudStorageService = cloudStorageService;
    }

    public Category findCategoryByName(CategoryRequest dto) {
        Optional<Category> categoriesOptional = categoryRepo.findCategoryByName(dto.categoryName());
        if(categoriesOptional.isPresent()) {
            return categoriesOptional.get();
        }

        return null;
    }

    public Category findCategoryById(Long categoryId) {
        Optional<Category> categoriesOptional = categoryRepo.findById(categoryId);
        if(!categoriesOptional.isPresent()){
            throw new NoSuchEntityException("Invalid Category Selected");
        }
        return categoriesOptional.get();
    }

    public void saveCategory(CategoryRequest dto, MultipartFile file) throws IOException {
        if(findCategoryByName(dto) != null){
            throw new DuplicateEntityException("This Category already exist");
        }

        if(!storageService.isValidFileExtension(file)) throw new InvalidFileTypeException();

        String fileName = storageService.generateFileName(file, UploadType.CATEGORY_IMAGE);
        storageService.upload(file, fileName);

        Category category = new Category();
        category.setName(dto.categoryName());
        category.setAvailable(true);
        category.setImageName(fileName);
        categoryRepo.save(category);
    }

    public void updateCategory(CategoryRequest dto, MultipartFile image) throws IOException {

        Category category = findCategoryById(dto.id());

        if(image != null && !image.isEmpty()){
            if (!storageService.isValidFileExtension(image)) throw new InvalidFileTypeException();
            String newImageName = storageService.generateFileName(image,UploadType.CATEGORY_IMAGE);
            storageService.update(image, category.getImageName(),newImageName, UploadType.CATEGORY_IMAGE);
            category.setImageName(newImageName);
        }

        category.setName(dto.categoryName());
        categoryRepo.save(category);
    }

    public Map<String,Object> getClientCategories(){
        Map<String,Object> response = new HashMap<>();
        response.put("categories",formatResponse(categoryRepo.findAll()));
        return response;
    }

    public Map<String, Object> getAllCategories(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        Page<Category> categories = categoryRepo.findAll(pageable);
        Map<String ,Object> response = new HashMap<>();
        response.put("categories",formatResponse(categories.getContent()));
        response.put("count",categories.getTotalElements());
        return response;
    }

    public List<CategoryResponse> getAllCategoriesOnly() {
        List<CategoryResponse> list = new ArrayList<>();
        for(Category category :categoryRepo.findAll()){
            list.add(new CategoryResponse(
                    category.getId(),
                    category.getName(),
                    null
            ));
        }
        return list;
    }

    @Transactional
    public void delete(long id) {
        Category category = findCategoryById(id);
        cloudStorageService.delete(category.getImageName());
        categoryRepo.deleteById(id);
    }

    public Map<String ,Object> getCategoryByName(String name, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        Page<Category> category = categoryRepo.findByNameContainingIgnoreCase(name,pageable);
        Map<String ,Object> response = new HashMap<>();
        response.put("categories",formatResponse(category.getContent()));
        response.put("count",category.getTotalElements());
        return response;
    }

    private List<CategoryResponse> formatResponse(List<Category> categoryList){
        List<CategoryResponse> list = new ArrayList<>();
        for(Category category :categoryList){
            list.add(new CategoryResponse(
                    category.getId(),
                    category.getName(),
                    storageService.download(category.getImageName())
            ));
        }
        return list;
    }
}

