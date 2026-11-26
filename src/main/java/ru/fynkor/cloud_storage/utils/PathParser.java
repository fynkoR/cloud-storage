package ru.fynkor.cloud_storage.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class PathParser {

    public static String getPathToResource(String path){
        if(getCountSlashInPath(path) == 1){
            return "/";
        }
        Path getPath = Paths.get(path);
        return normalizePathToDirectory(getPath.getParent().toString());
    }

    public static int getCountSlashInPath(String path){
        int count = 0;
        for(Character ch : path.toCharArray()){
            if(ch.equals('/')){
                count++;
            }
        }
        return count;
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
