package ru.fynkor.cloud_storage.dto;

import ru.fynkor.cloud_storage.enums.ResourceType;

public record FileResponse(
        String path,
        String name,
        Long size,
        ResourceType type
) implements ResourceResponse {
    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ResourceType getResourceType() {
        return this.type;
    }
}
