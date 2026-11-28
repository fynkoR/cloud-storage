package ru.fynkor.cloud_storage.services;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.fynkor.cloud_storage.dto.DirectoryResponse;
import ru.fynkor.cloud_storage.dto.FileResponse;
import ru.fynkor.cloud_storage.dto.ResourceResponse;
import ru.fynkor.cloud_storage.enums.ResourceType;
import ru.fynkor.cloud_storage.exceptions.PathException;
import ru.fynkor.cloud_storage.exceptions.ResourceNotFoundException;
import ru.fynkor.cloud_storage.utils.PathParser;
import ru.fynkor.cloud_storage.utils.Validator;

import java.io.InputStream;

@Service
public class ResourceService {

    private static final int BUFFER_SIZE = 8192;

    private final MinioService minioService;

    public ResourceService(MinioService minioService) {
        this.minioService = minioService;
    }

    public ResourceResponse getInfo(String path) {
        String trimPath = trimAndValidateExisting(path);

        String pathToResource = PathParser.getPathToResource(trimPath);
        String nameResource = PathParser.getNameResource(trimPath);

        if (trimPath.endsWith("/")) {
            return new DirectoryResponse(
                    pathToResource,
                    nameResource,
                    ResourceType.DIRECTORY
            );
        }

        Long size = minioService.getSizeFile(trimPath);
        return new FileResponse(
                pathToResource,
                nameResource,
                size,
                ResourceType.FILE
        );
    }

    public void delete(String path) {
        String trimPath = trimAndValidateExisting(path);

        minioService.deleteResource(trimPath);
    }

    public StreamingResponseBody download(String path) {
        String trimPath = trimAndValidateExisting(path);

        return outputStream -> {
            try (InputStream in = minioService.downloadResource(trimPath)) {

                byte[] buffer = new byte[BUFFER_SIZE];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }

            }
        };
    }

    private String trimAndValidateExisting(String path) {
        String errors = Validator.validatePath(path);
        if (!errors.isEmpty()) {
            throw new PathException(errors);
        }

        String trimPath = path.trim();

        if (!minioService.isResourceExists(trimPath)) {
            throw new ResourceNotFoundException(trimPath);
        }

        return trimPath;
    }
}
