package com.ddelight.ddAPI.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class CloudStorageConfig {

    @Value("${secret-key-path}")
    private String secret_key_pah;
    @Value("${bucket-name}")
    private String bucketName;

    @PostConstruct
    public void configure() throws IOException {
        File file = new File(secret_key_pah+ "secret.json");
        InputStream serviceAccount = new FileInputStream(file);
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setStorageBucket(bucketName)
                .build();
        FirebaseApp.initializeApp(options);
    }

    @Bean
    public Storage getStorageInstance(){
        return StorageClient.getInstance().bucket().getStorage();
    }

}
