package ru.fynkor.cloud_storage.exceptions;

public class UserYetExistsException extends RuntimeException {
    public UserYetExistsException(String username) {
        super("User with username: " + username + " yet exists !");
    }
}
