package ru.fynkor.cloud_storage.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUserDto(
        String username,
        String password) {
}
