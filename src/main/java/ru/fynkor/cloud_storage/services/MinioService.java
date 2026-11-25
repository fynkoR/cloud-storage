package ru.fynkor.cloud_storage.services;

import io.minio.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.fynkor.cloud_storage.props.MinioProperties;

import java.io.InputStream;

@Service
public class MinioService {
    private static final Long EMPTY_SIZE = 0L;
    private static final Long AUTO_PART_SIZE = -1L;

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final UserService userService;

    public MinioService(MinioClient minioClient, MinioProperties minioProperties, UserService userService) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
        this.userService = userService;
    }

    @SneakyThrows
    public void createBucket() {
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

    public boolean isParentFolderExists(String pathToDirectory) {

        String userPathToDirectory = getPathForUser(pathToDirectory);

        int lastIndexOfSlash = userPathToDirectory.lastIndexOf("/");

        if (lastIndexOfSlash <= 0) {
            return true;
        }

        return minioClient.listObjects(
                ListObjectsArgs
                        .builder()
                        .bucket(minioProperties.getBucket())
                        .prefix(userPathToDirectory)
                        .build()
        ).iterator().hasNext();
    }

    public boolean isFolderYetExists(String path) {
        String userPath = getPathForUser(path);

        return minioClient.listObjects(
                ListObjectsArgs
                        .builder()
                        .bucket(minioProperties.getBucket())
                        .prefix(userPath)
                        .build()
        ).iterator().hasNext();
    }

    @SneakyThrows
    public void saveDirectory(String path) {
        String userPath = getPathForUser(path);

        try (InputStream inputStream = InputStream.nullInputStream()) {
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(minioProperties.getBucket())
                    .object(userPath)
                    .stream(inputStream, EMPTY_SIZE, AUTO_PART_SIZE)
                    .build());
        }
    }

    private String getPathForUser(String path){
        Integer id = userService.getIdUserByAuthentication();
        return "user-" + id + "-files" + "/" + path;
    }
}
