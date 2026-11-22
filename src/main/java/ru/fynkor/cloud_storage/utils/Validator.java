package ru.fynkor.cloud_storage.utils;

import jakarta.validation.Valid;
import ru.fynkor.cloud_storage.exceptions.PathException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validator {

    private Validator(){}

    // TODO: придумать что то на счет boolean или сделать void

    public static boolean isValidPath(String path){
        if(path == null || path.isBlank() || !path.contains("/")){
            return false;
        }

        String targetCharsRegex = "[\\:*?\"<>|]";
        Pattern pattern = Pattern.compile(targetCharsRegex);
        Matcher matcher = pattern.matcher(path);

        return !matcher.find();
    }
}
