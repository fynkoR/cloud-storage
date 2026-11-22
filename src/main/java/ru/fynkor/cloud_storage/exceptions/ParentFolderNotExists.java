package ru.fynkor.cloud_storage.exceptions;

public class ParentFolderNotExists extends RuntimeException {
    public ParentFolderNotExists(String pathToDirectory) {
        super("Parent folder not exists for path: " + pathToDirectory);
    }
}
