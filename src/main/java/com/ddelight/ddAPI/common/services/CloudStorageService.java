package com.ddelight.ddAPI.common.services;

import com.ddelight.ddAPI.common.enums.UploadType;
import com.ddelight.ddAPI.common.exception.NoSuchImageException;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CloudStorageService {

    private final Map<String, String> mediaLinkCache = new ConcurrentHashMap<>();

    private Storage storage;
    @Value("${bucket-name}")
    private String bucketName;

    public CloudStorageService(Storage storage){
        this.storage = storage;
    }

    public String generateFileName(MultipartFile file, UploadType type){
        String fileName = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(UUID.randomUUID().toString().getBytes()).substring(0,20);
        fileName = addPathToName(fileName,type);
        return fileName+"."+getFileExtension(file);
    }

    public void upload(MultipartFile file,String fileName) throws IOException {

        InputStream fileInputStream = file.getInputStream();
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName).build();
        storage.create(blobInfo, fileInputStream);
    }

    public String download(String filename){
        if (mediaLinkCache.containsKey(filename)) {
            return mediaLinkCache.get(filename);
        }

        Blob blob = storage.get(BlobId.of(bucketName, filename));
        if (blob == null) {
            return null;
        }

        // Set ACL only if not already set (reduce repeated updates)
        blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        String mediaLink = blob.getMediaLink();
        mediaLinkCache.put(filename, mediaLink);  // Cache the link
        return mediaLink;
    }

    private String addPathToName(String fileName,UploadType type){
        return type.name()+"/"+fileName;
    }

    public String getFileExtension(MultipartFile uploadFile) {
        return uploadFile.getContentType().split("/")[1];
    }

    public boolean isValidFileExtension(MultipartFile uploadFile){
        return getFileExtension(uploadFile).equalsIgnoreCase("jpeg")||
                getFileExtension(uploadFile).equalsIgnoreCase("png");
    }

    public void update(MultipartFile image, String oldImageName, String newImageName, UploadType type) throws IOException {

        BlobId oldImageBlob = BlobId.of(bucketName,oldImageName);
//        BlobId newImageBlob = BlobId.of(bucketName,newImageName);

        if(oldImageName == null){
            upload(image,newImageName);
            return;
        }

//        storage.copy(Storage.CopyRequest.newBuilder().setSource(newImageBlob).setTarget(oldImageBlob).build());
        upload(image,newImageName);
        storage.get(oldImageBlob).delete();

    }

    public void delete(String oldImageName){
        BlobId oldimaBlobId = BlobId.of(bucketName,oldImageName);
        if(oldimaBlobId == null){
            throw new NoSuchImageException();
        }
        storage.delete(oldimaBlobId);
    }

}
