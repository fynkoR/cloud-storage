package ru.fynkor.cloud_storage.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fynkor.cloud_storage.dto.CreateUserDto;
import ru.fynkor.cloud_storage.dto.UserDto;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @PostMapping("/sign-up")
    public UserDto registration(@Valid @RequestBody CreateUserDto createUserDto){

    }
}
