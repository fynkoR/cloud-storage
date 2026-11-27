package ru.fynkor.cloud_storage.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import ru.fynkor.cloud_storage.dto.ResourceResponse;
import ru.fynkor.cloud_storage.services.DirectoryService;

@RestController
@RequestMapping("/api/directory")
@EnableMethodSecurity
public class DirectoryController {
    private final DirectoryService directoryService;

    public DirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResourceResponse> createDirectory(
            @RequestParam String path
    ){
        ResourceResponse resourceResponse = directoryService.create(path);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resourceResponse);
    }
}
