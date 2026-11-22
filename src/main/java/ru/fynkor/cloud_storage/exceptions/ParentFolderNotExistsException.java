package ru.fynkor.cloud_storage.exceptions;

public class ParentFolderNotExistsException extends RuntimeException {
    public ParentFolderNotExistsException(String pathToDirectory) {
        super("Parent folder not exists for path: " + pathToDirectory);
    }
}
