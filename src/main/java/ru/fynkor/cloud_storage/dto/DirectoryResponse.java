package ru.fynkor.cloud_storage.dto;

import ru.fynkor.cloud_storage.enums.ResourceType;

public record DirectoryResponse(
        String path,
        String name,
        ResourceType type
) implements ResourceResponse {
}
