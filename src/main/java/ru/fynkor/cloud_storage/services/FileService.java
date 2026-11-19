package ru.fynkor.cloud_storage.services;

import io.minio.MinioClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fynkor.cloud_storage.props.MinioProperties;

@Service
public class FileService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public FileService(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }


    // TODO : доделать аплоуд файла
    public String upload(){
        try{
            MultipartFile file
        }
    }
}
