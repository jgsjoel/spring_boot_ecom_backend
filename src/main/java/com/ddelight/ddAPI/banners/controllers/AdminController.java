package com.ddelight.ddAPI.banners.controllers;

import com.ddelight.ddAPI.banners.dtos.BannerRequest;
import com.ddelight.ddAPI.banners.services.BannerService;
import com.ddelight.ddAPI.category.services.CategoryService;
import com.ddelight.ddAPI.common.groups.Create;
import com.ddelight.ddAPI.common.groups.Delete;
import com.ddelight.ddAPI.common.groups.Update;
import com.ddelight.ddAPI.common.repositories.BannerRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/admin/banner")
public class AdminController {

    private BannerService bannerService;

    public AdminController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping
    public ResponseEntity<?> getBanners() {
        return new ResponseEntity<>(bannerService.getAllBanners(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addBanner(@Validated(Create.class) @ModelAttribute BannerRequest dto) throws IOException {
        bannerService.saveBanner(dto.title(), dto.file());
        return new ResponseEntity<>("Banner saved",HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateBanner(@Validated(Update.class) @ModelAttribute BannerRequest dto) throws IOException {
        bannerService.updateBanner(dto);
        return new ResponseEntity<>("Banner updated",HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteBanner(@Validated(Delete.class) @RequestBody BannerRequest dto) throws IOException {
        bannerService.deleteBannerById(dto.id());
        return new ResponseEntity<>("Banner deleted",HttpStatus.OK);
    }


}
