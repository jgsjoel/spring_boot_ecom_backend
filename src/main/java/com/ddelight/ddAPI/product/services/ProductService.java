package com.ddelight.ddAPI.product.services;

import com.ddelight.ddAPI.category.services.CategoryService;
import com.ddelight.ddAPI.common.entities.Category;
import com.ddelight.ddAPI.common.entities.Product;
import com.ddelight.ddAPI.common.enums.UploadType;
import com.ddelight.ddAPI.common.exception.DuplicateEntityException;
import com.ddelight.ddAPI.common.exception.InvalidFileTypeException;
import com.ddelight.ddAPI.common.exception.NoSuchEntityException;
import com.ddelight.ddAPI.common.repositories.CategoryRepo;
import com.ddelight.ddAPI.common.repositories.ProductRepo;
import com.ddelight.ddAPI.common.services.CloudStorageService;
import com.ddelight.ddAPI.product.dto.ProductRequest;
import com.ddelight.ddAPI.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductService {

    private ProductRepo productRepo;
    private CategoryRepo categoryRepo;
    private CloudStorageService storageService;
    private CategoryService categoryService;

    public ProductService(ProductRepo productRepo,
                          CategoryRepo categoryRepo,
                          CloudStorageService storageService,
                          CategoryService categoryService){
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.storageService = storageService;
        this.categoryService = categoryService;
    }

    public Map<String,Object> loadClientHomeProducts(){
        List<Product> products = productRepo.findTop5ByOrderByCreatedDesc();
        Map<String ,Object> response = new HashMap<>();
        response.put("products",formatResponse(products));
        return response;
    }

    public Map<String,Object> loadProducts(int pageNumber,int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> products = productRepo.findAll(pageable);
        Map<String ,Object> response = new HashMap<>();
        response.put("products",formatResponse(products.getContent()));
        response.put("count",products.getTotalElements());
        return response;
    }

    public boolean productExist(ProductRequest dto){
        Optional<Product> product = productRepo.findByName(dto.name());
        return (product.isPresent())? true:false;
    }

    public void saveProduct(ProductRequest dto, MultipartFile file) throws IOException {

        if(productExist(dto)){
            throw new DuplicateEntityException("Product with same name already exist");
        }

        if(!storageService.isValidFileExtension(file)) throw new InvalidFileTypeException();

        String fileName = storageService.generateFileName(file, UploadType.PRODUCT_IMAGE);

        Product product = new Product();
        product.setName(dto.name());
        product.setAvailableQty(dto.quantity());
        product.setCreated(LocalDateTime.now());
        product.setUpdated(LocalDateTime.now());
        product.setPrice(dto.price());
        product.setImageName(fileName);
        product.setDescription(dto.description());
        product.setWeight(dto.weight());
        product.setAvailable(true);
        product.setDiscount(dto.discount());

        Category category = categoryService.findCategoryById(dto.categoryId());
        product.setCategory(category);

        productRepo.save(product);
        storageService.upload(file, fileName);

    }

    public Product findProductById(Long id) {
        Optional<Product> productOptional = productRepo.findById(id);
        if(productOptional.isPresent()){
            return productOptional.get();
        }else{
            throw new NoSuchEntityException("Product with id "+id+" does not exist");
        }
    }

    public void updateProduct(ProductRequest dto, MultipartFile image) throws IOException {

        Product product = findProductById(dto.id());

        if(image != null && !image.isEmpty()){
            if (!storageService.isValidFileExtension(image)) throw new InvalidFileTypeException();
            String newImageName = storageService.generateFileName(image,UploadType.PRODUCT_IMAGE);
            storageService.update(image, product.getImageName(), newImageName, UploadType.PRODUCT_IMAGE);
            product.setImageName(newImageName);
        }

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setUpdated(LocalDateTime.now());
        product.setAvailableQty(dto.quantity());
        product.setPrice(dto.price());
        product.setDiscount(dto.discount());
        product.setWeight(dto.weight());
        product.setCategory(categoryService.findCategoryById(dto.categoryId()));

        productRepo.save(product);
    }

    public Map<String ,Object> findByName(String name,int pageNumber,int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Product> products = productRepo.findByNameContainingIgnoreCase(name,pageable);
        for(Product product :products.getContent()){
            System.out.println(product.getName());
        };
        Map<String ,Object> response = new HashMap<>();
        response.put("products",formatResponse(products.getContent()));
        response.put("count",products.getTotalElements());
        return response;
    }

    public Map<String ,Object> findByCategoryId(Long id,int pageNumber,int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Optional<Category> categoryOptional = categoryRepo.findById(id);
        if(!categoryOptional.isPresent()){
            throw new NoSuchEntityException("Invalid category provided");
        }
        Map<String ,Object> response = new HashMap<>();
        Page<Product> products = productRepo.findByCategory(categoryOptional.get(),pageable);
        response.put("products",formatResponse(products.getContent()));
        response.put("count",products.getTotalElements());
        return response;
    }

    public Map<String ,Object> getByCategoryIdAndProductName(String name, Long categoryId, int pageNumber,int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Optional<Category> categoryOptional = categoryRepo.findById(categoryId);
        if(!categoryOptional.isPresent()){
            throw new NoSuchEntityException("Invalid category provided");
        }
        Map<String ,Object> response = new HashMap<>();
        Page<Product> products = productRepo.findByNameContainingIgnoreCaseAndCategory(name,categoryOptional.get(),pageable);
        response.put("products",formatResponse(products.getContent()));
        response.put("count",products.getTotalElements());
        return response;
    }

    public ProductResponse getSingleProductById(Long productId){
        Product product = findProductById(productId);
        return new ProductResponse(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAvailableQty(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getDiscount(),
                product.getWeight(),
                product.isAvailable(),
                storageService.download(product.getImageName()));
    }

    private List<ProductResponse> formatResponse(List<Product> productList){
        List<ProductResponse> list = new ArrayList<>();
        for(Product product :productList){
            list.add(new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getAvailableQty(),
                    product.getPrice(),
                    product.getCategory().getId(),
                    product.getDiscount(),
                    product.getWeight(),
                    product.isAvailable(),
                    storageService.download(product.getImageName())
            ));
        }
        return list;
    }

    public List<ProductResponse> findRelatedProducts(Long categoryId,Long productId){
        return formatResponse(productRepo.findTop5ByCategoryIdAndIdNot(categoryId,productId));
    }

    public void changeStatus(Long id) {
        Product product = findProductById(id);
        if ((product.isAvailable())) {
            product.setAvailable(false);
        } else {
            product.setAvailable(true);
        }
        productRepo.save(product);
    }

    public Map<String,Object> loadDiscountedItems() {
        Map<String,Object> discountedItems = new HashMap<>();
        discountedItems.put("products",formatResponse(productRepo.findByDiscountNot(0)));
        return discountedItems;
    }
}
