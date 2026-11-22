package ru.fynkor.cloud_storage.exceptions;

public class FolderYetExistsException extends RuntimeException {
    public FolderYetExistsException(String nameDirectory) {
        super("Folder yet exists with name: " + nameDirectory);
    }
}
