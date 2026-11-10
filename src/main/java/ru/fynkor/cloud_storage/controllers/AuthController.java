package ru.fynkor.cloud_storage.controllers;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fynkor.cloud_storage.dto.CreateUserDto;
import ru.fynkor.cloud_storage.dto.UserDto;
import ru.fynkor.cloud_storage.services.RegistrationService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegistrationService registrationService;

    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }


    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> registration(@Valid @RequestBody CreateUserDto createUserDto){
        String username = registrationService.registration(createUserDto);
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(new UserDto(username));
    }
}
