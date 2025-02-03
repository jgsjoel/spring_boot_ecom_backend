package com.ddelight.ddAPI.banners.services;

import com.ddelight.ddAPI.banners.dtos.BannerRequest;
import com.ddelight.ddAPI.banners.dtos.BannerResponse;
import com.ddelight.ddAPI.common.entities.Banner;
import com.ddelight.ddAPI.common.entities.Category;
import com.ddelight.ddAPI.common.entities.Product;
import com.ddelight.ddAPI.common.enums.UploadType;
import com.ddelight.ddAPI.common.exception.DuplicateEntityException;
import com.ddelight.ddAPI.common.exception.InvalidFileTypeException;
import com.ddelight.ddAPI.common.exception.NoSuchEntityException;
import com.ddelight.ddAPI.common.repositories.BannerRepo;
import com.ddelight.ddAPI.common.services.CloudStorageService;
import com.ddelight.ddAPI.product.dto.ProductResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class BannerService {


    private final CloudStorageService cloudStorageService;
    private BannerRepo bannerRepo;
    private CloudStorageService storageService;
    private final int PAGE_SIZE = 2;

    public BannerService(
            BannerRepo bannerRepo,
            CloudStorageService storageService,
            CloudStorageService cloudStorageService) {
        this.bannerRepo = bannerRepo;
        this.storageService = storageService;
        this.cloudStorageService = cloudStorageService;
    }

    private Banner findBannerById(Long id) {
        Optional<Banner> bannerOptional = bannerRepo.findById(id);
        if(bannerOptional.isPresent()){
            return bannerOptional.get();
        }else{
            throw new NoSuchEntityException("Banner with id "+id+" does not exist");
        }
    }

    public Map<String, Object> getAllBanners() {
        List<Banner> banners = bannerRepo.findAll();
        Map<String ,Object> response = new HashMap<>();
        response.put("banners",formatResponse(banners));
        return response;
    }

    public void saveBanner(String title, MultipartFile file) throws IOException {

        Banner banner = new Banner();

        if(file != null && !file.isEmpty()){
            if(!storageService.isValidFileExtension(file)) throw new InvalidFileTypeException();
            String fileName = storageService.generateFileName(file, UploadType.BANNER_IMAGE);
            storageService.upload(file, fileName);
            banner.setImageName(fileName);
        }

        banner.setTitle(title);
        bannerRepo.save(banner);
    }

    public void updateBanner(BannerRequest dto) throws IOException {
        Banner banner = findBannerById(dto.id());

        if(dto.file() != null && !dto.file().isEmpty()){
            if (!storageService.isValidFileExtension(dto.file())) throw new InvalidFileTypeException();
            String newImageName = storageService.generateFileName(dto.file(),UploadType.BANNER_IMAGE);
            storageService.update(dto.file(), banner.getImageName(), newImageName, UploadType.BANNER_IMAGE);
            banner.setImageName(newImageName);
        }

        banner.setTitle(dto.title());
        bannerRepo.save(banner);
    }

    @Transactional
    public void deleteBannerById(Long id) {
        Banner banner = findBannerById(id);
        storageService.delete(banner.getImageName());
        bannerRepo.deleteById(id);
    }

    private List<BannerResponse> formatResponse(List<Banner> bannerList){
        List<BannerResponse> list = new ArrayList<>();
        for(Banner banner :bannerList){
            list.add(new BannerResponse(
                    banner.getId(),
                    banner.getTitle(),
                    storageService.download(banner.getImageName())
            ));
        }
        return list;
    }
}
