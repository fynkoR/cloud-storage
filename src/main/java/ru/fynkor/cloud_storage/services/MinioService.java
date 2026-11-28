package ru.fynkor.cloud_storage.services;

import com.google.common.collect.ImmutableList;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fynkor.cloud_storage.props.MinioProperties;
import ru.fynkor.cloud_storage.utils.PathParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
public class MinioService {
    private static final Long EMPTY_SIZE = 0L;
    private static final Long AUTO_PART_SIZE = -1L;
    private static final Long MAX_SIZE_FILE = 20L * 1024 * 1024;

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

        return minioClient.listObjects(ListObjectsArgs
                .builder()
                .bucket(minioProperties.getBucket())
                .prefix(userPathToDirectory)
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

    @SneakyThrows
    public void saveFile(MultipartFile file, String path) {
        String userPath = getPathForUser(path);
        String userPathWithFileName = userPath + "/" + file.getOriginalFilename();
        minioClient.putObject(PutObjectArgs
                .builder()
                .bucket(minioProperties.getBucket())
                .stream(file.getInputStream(), file.getSize(), MAX_SIZE_FILE)
                .object(userPathWithFileName)
                .build()
        );
    }

    public boolean isResourceExists(String path) {
        String userPath = getPathForUser(path);

        if (!path.endsWith("/")) {
            try {
                minioClient.statObject(StatObjectArgs
                        .builder()
                        .bucket(minioProperties.getBucket())
                        .object(userPath)
                        .build()
                );
                return true;
            } catch (MinioException exception) {
                return false;
            }
        }

        return minioClient.listObjects(ListObjectsArgs
                .builder()
                .bucket(minioProperties.getBucket())
                .prefix(userPath)
                .build()
        ).iterator().hasNext();
    }

    @SneakyThrows
    public Long getSizeFile(String path) {
        String userPath = getPathForUser(path);

        return minioClient.statObject(StatObjectArgs
                .builder()
                .bucket(minioProperties.getBucket())
                .object(userPath)
                .build()
        ).size();
    }

    @SneakyThrows
    public void deleteResource(String path) {
        String userPath = getPathForUser(path);
        String parentPath = PathParser.getPathToResource(path);

        List<Result<Item>> listObjects = getListObjects(parentPath);

        minioClient.removeObject(RemoveObjectArgs
                .builder()
                .bucket(minioProperties.getBucket())
                .object(userPath)
                .build()
        );

        if(listObjects.size() <= 1){
            saveDirectory(parentPath);
        }
    }

    private List<Result<Item>> getListObjects(String parentPath){
        Iterable<Result<Item>> listObjects = minioClient.listObjects(ListObjectsArgs
                .builder()
                .bucket(minioProperties.getBucket())
                .prefix(parentPath)
                .build()
        );

        return ImmutableList.copyOf(listObjects);
    }

    private String getPathForUser(String path) {
        Integer id = userService.getIdUserByAuthentication();
        return "user-" + id + "-files" + "/" + path;
    }
}
