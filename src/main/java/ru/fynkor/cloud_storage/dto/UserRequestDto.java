package ru.fynkor.cloud_storage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestDto(
        @NotBlank(message = "Username should be not empty")
        @Size(min = 3, max = 20, message = "Username should be between 3 and 20 characters")
        @Pattern(
                regexp = "^[\\p{L}\\d][\\p{L}\\d.()_%$?#@^&*\\-]{2,19}$",
                message = "Invalid username: 2-20 chars, must start with letter/digit"
        )
        String username,
        @NotBlank(message = "Password should be not empty")
        String password) {
}
