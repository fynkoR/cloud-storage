package ru.fynkor.cloud_storage.dto;

import jakarta.validation.constraints.NotBlank;

// TODO: необходимо сделать ограничения @NotEmpty, @Size ...

public record CreateUserDto(
        String username,
        String password) {
}
