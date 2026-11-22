package ru.fynkor.cloud_storage.services;

import io.minio.*;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.fynkor.cloud_storage.dto.DirectoryResponse;
import ru.fynkor.cloud_storage.enums.ResourceType;
import ru.fynkor.cloud_storage.exceptions.CreateDirectoryException;
import ru.fynkor.cloud_storage.exceptions.ParentFolderNotExists;
import ru.fynkor.cloud_storage.exceptions.PathException;
import ru.fynkor.cloud_storage.props.MinioProperties;
import ru.fynkor.cloud_storage.utils.Validator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DirectoryService {
    private static final Long EMPTY_SIZE = 0L;
    private static final Long AUTO_PART_SIZE = -1L;

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;


    public DirectoryService(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    // TODO: сделать ошибки
    //  404 - Родительская папка не существует
    //  409 - папка уже существует

    public DirectoryResponse create(String path){
        try{
            createBucket();
        }catch (Exception e){
            throw new CreateDirectoryException("Create directory failed: " + e.getMessage());
        }
        String errors = Validator.validatePath(path);

        if(!errors.isEmpty()){
            throw new PathException(errors);
        }

        String trimPath = path.trim();
        String pathToDirectory = getPathToDirectory(trimPath);

        if(!isParentFolderExists(pathToDirectory)){
            throw new ParentFolderNotExists(pathToDirectory);
        }

        saveDirectory(trimPath);

        String nameDirectory = getNameDirectory(trimPath);
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

    private String getPathToDirectory(String path){
        if(getCountSlashInPath(path) == 1){
            return "/";
        }
        Path getPath = Paths.get(path);
        return normalizePathToDirectory(getPath.getParent().toString());
    }

    private int getCountSlashInPath(String path){
        int count = 0;
        for(Character ch : path.toCharArray()){
            if(ch.equals('/')){
                count++;
            }
        }
        return count;
    }

    private String normalizePathToDirectory(String getPath){
        getPath += "/";
        return getPath.replace("\\", "/");
    }

    private boolean isParentFolderExists(String pathToDirectory){
        int lastIndexOfSlash = pathToDirectory.lastIndexOf("/");

        if(lastIndexOfSlash <= 0){
            return true;
        }

        String parentPrefix = pathToDirectory.substring(0, lastIndexOfSlash);

        return minioClient.listObjects(
                ListObjectsArgs
                        .builder()
                        .bucket(minioProperties.getBucket())
                        .prefix(parentPrefix)
                        .build()
        ).iterator().hasNext();
    }

    private String getNameDirectory(String path){
        Path getPath = Paths.get(path);
        return getPath.getFileName().toString();
    }

    @SneakyThrows
    private void saveDirectory(String path){
        try(InputStream inputStream = InputStream.nullInputStream()) {
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(minioProperties.getBucket())
                    .object(path)
                    .stream(inputStream, EMPTY_SIZE, AUTO_PART_SIZE)
                    .build());
        }
    }
}
