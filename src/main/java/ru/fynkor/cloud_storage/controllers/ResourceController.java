package ru.fynkor.cloud_storage.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.fynkor.cloud_storage.dto.UploadResponse;
import ru.fynkor.cloud_storage.services.FileService;

@RestController
@RequestMapping("/api/resource")
@EnableMethodSecurity
public class ResourceController {
    private final FileService fileService;

    public ResourceController(FileService fileService) {
        this.fileService = fileService;
    }


    // TODO: сделать ошибки

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UploadResponse> upload(
            @RequestParam(required = false) String path,
            @ModelAttribute MultipartFile file
    ) {
        UploadResponse uploadResponse = fileService.upload(path, file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(uploadResponse);
    }
}

