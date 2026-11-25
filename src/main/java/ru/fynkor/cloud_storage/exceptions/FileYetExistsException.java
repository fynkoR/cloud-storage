package ru.fynkor.cloud_storage.exceptions;

public class FileYetExistsException extends RuntimeException {
    public FileYetExistsException(String message) {
        super(message);
    }
}
