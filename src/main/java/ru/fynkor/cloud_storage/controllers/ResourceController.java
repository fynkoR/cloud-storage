package ru.fynkor.cloud_storage.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.fynkor.cloud_storage.dto.ResourceResponse;
import ru.fynkor.cloud_storage.services.FileService;
import ru.fynkor.cloud_storage.services.ResourceService;
import ru.fynkor.cloud_storage.utils.PathParser;

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

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResourceResponse> upload(
            @RequestParam(defaultValue = "") String path,
            @ModelAttribute MultipartFile file
    ) {
        ResourceResponse resourceResponse = fileService.upload(path, file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resourceResponse);
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

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(
            @RequestParam String path
    ){
        resourceService.delete(path);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/download")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StreamingResponseBody> download(
            @RequestParam String path
    ){
        StreamingResponseBody stream = resourceService.download(path);

        HttpHeaders headers = new HttpHeaders();
        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + PathParser.getNameResource(path)
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .headers(headers)
                .body(stream);
    }
}

