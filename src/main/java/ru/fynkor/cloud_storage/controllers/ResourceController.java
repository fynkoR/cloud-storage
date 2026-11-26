package ru.fynkor.cloud_storage.controllers;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.fynkor.cloud_storage.dto.ResourceResponse;
import ru.fynkor.cloud_storage.dto.UploadResponse;
import ru.fynkor.cloud_storage.enums.ResourceType;
import ru.fynkor.cloud_storage.services.FileService;
import ru.fynkor.cloud_storage.services.ResourceService;

@RestController
@RequestMapping("/api/resource")
@EnableMethodSecurity
public class ResourceController {
    private final FileService fileService;
    private final ResourceService resourceService;

    public ResourceController(FileService fileService, ResourceService resourceService) {
        this.fileService = fileService;
        this.resourceService = resourceService;
    }

    // TODO: решить на счет папок , проверка на символы и т.д.

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UploadResponse> upload(
            @RequestParam(defaultValue = "") String path,
            @ModelAttribute MultipartFile file
    ) {
        UploadResponse uploadResponse = fileService.upload(path, file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(uploadResponse);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResourceResponse> getInfo(
            @RequestParam(defaultValue = "") String path
    ){
        ResourceResponse resourceResponse = resourceService.getInfo(path);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resourceResponse);
    }
}

