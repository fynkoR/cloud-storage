package ru.fynkor.cloud_storage.dto;

import ru.fynkor.cloud_storage.enums.ResourceType;

public interface ResourceResponse {
        String getPath();
        String getName();
        ResourceType getResourceType();
}
