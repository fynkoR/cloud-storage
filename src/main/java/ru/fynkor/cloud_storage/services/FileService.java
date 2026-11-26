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
import ru.fynkor.cloud_storage.exceptions.FileYetExistsException;
import ru.fynkor.cloud_storage.exceptions.PathException;
import ru.fynkor.cloud_storage.exceptions.UploadException;
import ru.fynkor.cloud_storage.props.MinioProperties;
import ru.fynkor.cloud_storage.utils.Validator;

@Service
public class FileService {
    private final MinioService minioService;

    public FileService(MinioService minioService) {
        this.minioService = minioService;
    }

    public UploadResponse upload(String path, MultipartFile file) {
        try {
            minioService.createBucket();
        } catch (Exception e) {
            throw new UploadException("File upload failed: " + e.getMessage());
        }
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new UploadException("File is empty. File name must have name");
        }
        String errors = "";
        if(!path.isEmpty()){
            errors = Validator.validatePathToDirectory(path);
        }
        if(!errors.isEmpty()){
            throw new PathException(errors);
        }

        String pathWithFilename = path + file.getOriginalFilename();

        if(minioService.isFileYetExists(pathWithFilename)){
            throw new FileYetExistsException("File yet exists: " + file.getOriginalFilename());
        }

        minioService.saveFile(file, path);

        return new UploadResponse(
                path,
                file.getOriginalFilename(),
                file.getSize(),
                ResourceType.FILE
        );
    }
}
