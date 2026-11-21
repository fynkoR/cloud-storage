package ru.fynkor.cloud_storage.services;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.fynkor.cloud_storage.dto.DirectoryResponse;
import ru.fynkor.cloud_storage.enums.ResourceType;
import ru.fynkor.cloud_storage.exceptions.CreateDirectoryException;
import ru.fynkor.cloud_storage.props.MinioProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DirectoryService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;


    public DirectoryService(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    // TODO: доделать метод создания папки

    public DirectoryResponse create(String path){
        try{
            createBucket();
        }catch (Exception e){
            throw new CreateDirectoryException("Create directory failed: " + e.getMessage());
        }

        String pathToDirectory = getPathToDirectory(path);
        String nameDirectory = getNameDirectory(path);

        saveDirectory(path);
        return new DirectoryResponse(
                pathToDirectory,
                nameDirectory,
                ResourceType.DIRECTORY
        );
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

    // TODO : допилить метод
    private String getPathToDirectory(String path){
        Path pathParent = Paths.get(path);
        return pathParent.getParent().toString();
    }
}
