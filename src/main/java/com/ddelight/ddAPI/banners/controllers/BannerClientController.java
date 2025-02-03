package com.ddelight.ddAPI.banners.controllers;

import com.ddelight.ddAPI.banners.services.BannerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/banner")
public class BannerClientController {

    private BannerService bannerService;

    public BannerClientController(BannerService bannerService) {
        this.bannerService = bannerService;
    }


    @GetMapping
    public ResponseEntity<?> getBanners(){
        return new ResponseEntity<>(bannerService.getAllBanners(), HttpStatus.OK);
    }

}
