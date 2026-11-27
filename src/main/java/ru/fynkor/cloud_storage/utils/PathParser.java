package ru.fynkor.cloud_storage.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class PathParser {

    public static String getPathToResource(String path){
        Path getPath = Paths.get(path);
        if(getPath.getParent() == null){
            return "/";
        }
        return normalizePathToDirectory(getPath.getParent().toString());
    }

    public static String normalizePathToDirectory(String getPath){
        getPath += "/";
        return getPath.replace("\\", "/");
    }

    public static String getNameResource(String path){
        Path getPath = Paths.get(path);
        return getPath.getFileName().toString();
    }
}
