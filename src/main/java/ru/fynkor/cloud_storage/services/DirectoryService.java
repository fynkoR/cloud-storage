package ru.fynkor.cloud_storage.services;

import io.minio.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.fynkor.cloud_storage.dto.DirectoryResponse;
import ru.fynkor.cloud_storage.enums.ResourceType;
import ru.fynkor.cloud_storage.exceptions.CreateDirectoryException;
import ru.fynkor.cloud_storage.exceptions.FolderYetExistsException;
import ru.fynkor.cloud_storage.exceptions.ParentFolderNotExistsException;
import ru.fynkor.cloud_storage.exceptions.PathException;
import ru.fynkor.cloud_storage.props.MinioProperties;
import ru.fynkor.cloud_storage.utils.Validator;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DirectoryService {

    private final MinioService minioService;

    public DirectoryService(MinioService minioService) {
        this.minioService = minioService;
    }

    public DirectoryResponse create(String path){
        try{
            minioService.createBucket();
        }catch (Exception e){
            throw new CreateDirectoryException("Create directory failed: " + e.getMessage());
        }

        String errors = Validator.validatePath(path);

        if(!errors.isEmpty()){
            throw new PathException(errors);
        }

        String trimPath = path.trim();
        String pathToDirectory = getPathToDirectory(trimPath);
        String nameDirectory = getNameDirectory(trimPath);

        if(!minioService.isParentFolderExists(pathToDirectory)){
            throw new ParentFolderNotExistsException(pathToDirectory);
        }

        if(minioService.isFolderYetExists(trimPath)){
            throw new FolderYetExistsException(nameDirectory);
        }

        minioService.saveDirectory(trimPath);

        return new DirectoryResponse(
                pathToDirectory,
                nameDirectory,
                ResourceType.DIRECTORY
        );
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

    private String getNameDirectory(String path){
        Path getPath = Paths.get(path);
        return getPath.getFileName().toString();
    }
}
