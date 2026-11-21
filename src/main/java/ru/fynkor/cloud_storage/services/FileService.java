package ru.fynkor.cloud_storage.services;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fynkor.cloud_storage.dto.UploadResponse;
import ru.fynkor.cloud_storage.enums.ResourceType;
import ru.fynkor.cloud_storage.exceptions.UploadException;
import ru.fynkor.cloud_storage.props.MinioProperties;

@Service
public class FileService {
    private static final Long MAX_SIZE_FILE = 20L * 1024 * 1024;

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public FileService(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    // @Transactional
    public UploadResponse upload(String path, MultipartFile file) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new UploadException("File upload failed: " + e.getMessage());
        }
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new UploadException("File is empty. File name must have name");
        }
        String fileName = file.getOriginalFilename();
        saveFile(file, fileName);
        return toUploadResponse(file, path);
    }

    @SneakyThrows
    private void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build()
        );
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs
                    .builder()
                    .bucket(minioProperties.getBucket())
                    .build()
            );
        }
    }

    @SneakyThrows
    private void saveFile(MultipartFile file, String fileName) {
        minioClient.putObject(PutObjectArgs
                .builder()
                .stream(file.getInputStream(), file.getSize(), MAX_SIZE_FILE)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build()
        );
    }

    private UploadResponse toUploadResponse(MultipartFile file, String path){
        ResourceType resourceType;
        if(path == null){
            path = "";
        }
        if(path.endsWith("/")){
            resourceType = ResourceType.DIRECTORY;
        }
        else{
            resourceType = ResourceType.FILE;
        }
        return new UploadResponse(
                path,
                file.getOriginalFilename(),
                file.getSize(),
                resourceType
        );
    }
}
