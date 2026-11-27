package ru.fynkor.cloud_storage.services;

import io.minio.*;
import org.springframework.stereotype.Service;
import ru.fynkor.cloud_storage.dto.DirectoryResponse;
import ru.fynkor.cloud_storage.dto.ResourceResponse;
import ru.fynkor.cloud_storage.enums.ResourceType;
import ru.fynkor.cloud_storage.exceptions.CreateDirectoryException;
import ru.fynkor.cloud_storage.exceptions.FolderYetExistsException;
import ru.fynkor.cloud_storage.exceptions.ParentFolderNotExistsException;
import ru.fynkor.cloud_storage.exceptions.PathException;
import ru.fynkor.cloud_storage.utils.PathParser;
import ru.fynkor.cloud_storage.utils.Validator;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DirectoryService {

    private final MinioService minioService;

    public DirectoryService(MinioService minioService) {
        this.minioService = minioService;
    }

    public ResourceResponse create(String path){
        try{
            minioService.createBucket();
        }catch (Exception e){
            throw new CreateDirectoryException("Create directory failed: " + e.getMessage());
        }

        String errors = Validator.validatePathToDirectory(path);

        if(!errors.isEmpty()){
            throw new PathException(errors);
        }

        String trimPath = path.trim();
        String pathToDirectory = PathParser.getPathToResource(trimPath);
        String nameDirectory = PathParser.getNameResource(trimPath);

        if(!minioService.isParentFolderExists(pathToDirectory)){
            throw new ParentFolderNotExistsException(pathToDirectory);
        }

        if(minioService.isResourceExists(trimPath)){
            throw new FolderYetExistsException(nameDirectory);
        }

        minioService.saveDirectory(trimPath);

        return new DirectoryResponse(
                pathToDirectory,
                nameDirectory,
                ResourceType.DIRECTORY
        );
    }
}
