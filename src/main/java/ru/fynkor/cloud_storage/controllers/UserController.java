package ru.fynkor.cloud_storage.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fynkor.cloud_storage.dto.UserResponseDto;
import ru.fynkor.cloud_storage.security.CustomUserDetails;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> showInfoUser(){

        // TODO: надо будет это убрать в один из сервисов

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new UserResponseDto(username));
    }
}
