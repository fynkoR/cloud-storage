package ru.fynkor.cloud_storage.services;

import org.springframework.stereotype.Service;
import ru.fynkor.cloud_storage.dto.UploadResponse;

@Service
public class ResourceService {

    private final MinioService minioService;


    public ResourceService(MinioService minioService) {
        this.minioService = minioService;
    }

    public UploadResponse getInfo(String path){

    }
}
