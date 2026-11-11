package ru.fynkor.cloud_storage.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fynkor.cloud_storage.dto.UserResponseDto;
import ru.fynkor.cloud_storage.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> showInfoUser(){
        String username = userService.getUsernameByAuthentication();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new UserResponseDto(username));
    }
}
