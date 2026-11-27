package ru.fynkor.cloud_storage.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validator {

    private static final String TARGET_CHARS_REGEX = "[\\\\:*?\"<>|]";

    private Validator() {
    }

    public static String validatePath(String path) {
        if (!path.contains("/")) {
            return validateFilename(path);
        }
        if (path.endsWith("/")) {
            return validatePathToDirectory(path);
        }

        int lastIndexOfSlash = path.lastIndexOf("/");
        String parentPath = path.substring(0, lastIndexOfSlash) + "/";
        String filename = path.substring(lastIndexOfSlash + 1);

        return validatePathToDirectory(parentPath) + validateFilename(filename);
    }

    public static String validatePathToDirectory(String path) {
        List<String> errors = new ArrayList<>();

        if (path == null || path.isBlank()) {
            return "Path must be null or blank";
        }

        String trimPath = path.trim();

        if (trimPath.length() == 1) {
            errors.add("Path should be > 1");
        }
        if (trimPath.length() >= 255) {
            errors.add("Path should be < 255 chars");
        }
        if (trimPath.startsWith("/")) {
            errors.add("Path should be not start with </>");
        }
        if (!trimPath.endsWith("/")) {
            errors.add("Path should be end with </>");
        }
        if (hasDoubleSlash(trimPath)) {
            errors.add("In path should not be empty folder");
        }

        Pattern pattern = Pattern.compile(TARGET_CHARS_REGEX);
        Matcher matcher = pattern.matcher(trimPath);
        if (matcher.find()) {
            errors.add("Path shouldn't be: \\ , : , * , ? , \" , < , > , | ");
        }

        return String.join(", ", errors);
    }

    private static boolean hasDoubleSlash(String path) {
        char[] arrayFromPath = path.toCharArray();
        for (int i = 0; i < arrayFromPath.length - 1; i++) {
            if (Objects.equals(arrayFromPath[i], '/') &&
                    Objects.equals(arrayFromPath[i], arrayFromPath[i + 1])
            ) {
                return true;
            }
        }
        return false;
    }

    public static String validateFilename(String filename) {
        List<String> errors = new ArrayList<>();

        String trimFilename = filename.trim();

        if(trimFilename.isEmpty()){
            errors.add("Filename shouldn't is empty");
        }
        if (trimFilename.endsWith(".")) {
            errors.add("Filename shouldn't ends with <.>");
        }
        if (trimFilename.length() > 255) {
            errors.add("Filename should be < 255 chars");
        }
        Pattern pattern = Pattern.compile(TARGET_CHARS_REGEX);
        Matcher matcher = pattern.matcher(trimFilename);
        if (matcher.find()) {
            errors.add("Filename shouldn't be: \\ , : , * , ? , \" , < , > , | ");
        }

        return String.join(", ", errors);
    }
}
