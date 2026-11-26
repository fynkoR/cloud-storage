package ru.fynkor.cloud_storage.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String path) {
        super("Resource with path: " + path + " not found");
    }
}
