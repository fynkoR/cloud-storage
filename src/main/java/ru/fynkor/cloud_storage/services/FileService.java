package ru.fynkor.cloud_storage.services;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.fynkor.cloud_storage.entities.Resource;
import ru.fynkor.cloud_storage.exceptions.UploadException;
import ru.fynkor.cloud_storage.props.MinioProperties;

import java.io.InputStream;

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
    // TODO : доделать аплоуд файла
    public String upload(Resource resource) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new UploadException("File upload failed: " + e.getMessage());
        }
        MultipartFile file = resource.getFile();
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new UploadException("File is empty. File name must have name");
        }
        String fileName = file.getName();
        InputStream inputStream;
        /*try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            throw new UploadException("File upload failed: " + e.getMessage());
        }*/
        saveFile(file, fileName);
        return fileName;
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

    /*private String generateFileName(MultipartFile file){
        String extension = getExtension(file);
        return
    }*/
}
