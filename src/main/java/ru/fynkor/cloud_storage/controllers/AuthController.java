package ru.fynkor.cloud_storage.controllers;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fynkor.cloud_storage.dto.UserRequestDto;
import ru.fynkor.cloud_storage.dto.UserResponseDto;
import ru.fynkor.cloud_storage.services.AuthService;
import ru.fynkor.cloud_storage.services.RegistrationService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final AuthService authService;

    public AuthController(RegistrationService registrationService, AuthService authService) {
        this.registrationService = registrationService;
        this.authService = authService;
    }


    @PostMapping("/sign-up")
    public ResponseEntity<UserResponseDto> registration(@Valid @RequestBody UserRequestDto userRequestDto){
        String username = registrationService.registration(userRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new UserResponseDto(username));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserResponseDto> authentication(@Valid @RequestBody UserRequestDto userRequestDto){
        String username = authService.performAuthentication(userRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new UserResponseDto(username));
    }
}
