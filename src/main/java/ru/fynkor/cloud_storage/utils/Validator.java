package ru.fynkor.cloud_storage.utils;

import jakarta.validation.Valid;
import ru.fynkor.cloud_storage.exceptions.PathException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Validator {

    private static final String TARGET_CHARS_REGEX = "[\\\\:*?\"<>|]";

    private Validator(){}

    public static String validatePath(String path){
        List<String> errors = new ArrayList<>();

        if(path == null || path.isBlank()){
            return "Path must be null or blank";
        }

        String trimPath = path.trim();

        if(trimPath.length() == 1){
            errors.add("Path should be > 1");
        }
        if(trimPath.length() >= 255){
            errors.add("Path should be < 255 chars");
        }
        if(!trimPath.endsWith("/")){
            errors.add("Path should be end with </>");
        }

        Pattern pattern = Pattern.compile(TARGET_CHARS_REGEX);
        Matcher matcher = pattern.matcher(trimPath);
        if(matcher.find()){
            errors.add("Path shouldn't be: \\ , : , * , ? , \" , < , > , | ");
        }

        return String.join(", ", errors);
    }
}
