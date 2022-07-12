package org.test.codegen.app.utils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaUtils {
    private static final Pattern pattern = Pattern.compile("List<(.*)>");

    public static String toClassName(final String name) {
        String returnStr = name.replaceAll("\\s", "");
        StringBuilder builder = new StringBuilder();
        builder.append(returnStr.substring(0, 1).toUpperCase());
        builder.append(returnStr.substring(1));
        return builder.toString();
    }

    public static String removeList(final String listClass) {
        Matcher matcher = pattern.matcher(listClass);
        matcher.matches();
        return matcher.group(1);
    }

    public static String toListClassName(String className) {
        return String.format("List<%s>", className);
    }

    public static String toPropName(String name) {
        StringBuilder builder = new StringBuilder();
        builder.append(name.substring(0, 1).toLowerCase());
        builder.append(name.substring(1));
        return builder.toString();
    }

    public static Path toPath(String packageName, String className) {
        return Path.of("src/main/java", fromPackageToPath(packageName), className + ".java");
    }

    public static String fromPackageToPath(String packageName) {
        return packageName.replace(".", "/");
    }

    public static String toType(String name, String schemaType) {
        if ("string".equals(schemaType)) {
            return "String";
        } else if ("integer".equals(schemaType)) {
            return "int";
        } else if ("object".equals(schemaType)) {
            return toClassName(name);
        } else if ("array".equals(schemaType)) {
            return "List<" + toClassName(name) + ">";
        } else
            return "String";
    }

    public static String toPackage(String basePackage, String... currPackage) {
        List<String> packages = new ArrayList<>();
        packages.add(basePackage);
        packages.addAll(Arrays.asList(currPackage));
        return String.join(".", packages.toArray(new String[0]));
    }
}
