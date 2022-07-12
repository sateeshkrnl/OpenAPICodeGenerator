package org.test.codegen.app.utils;

import io.swagger.v3.oas.models.media.Schema;

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
        String builder = returnStr.substring(0, 1).toUpperCase() +
                returnStr.substring(1);
        return builder;
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
        String builder = name.substring(0, 1).toLowerCase() +
                name.substring(1);
        return builder;
    }

    public static Path toPath(String packageName, String className) {
        return Path.of("src/main/java", fromPackageToPath(packageName), className + ".java");
    }

    public static String fromPackageToPath(String packageName) {
        return packageName.replace(".", "/");
    }

    public static String toType(String name, Schema schema) {
        String schemaType=schema.getType();
        if ("string".equals(schemaType)) {
            return "String";
        } else if ("integer".equals(schemaType)) {
            return "int";
        } else if ("object".equals(schemaType)) {
            return toClassName(name);
        } else if ("array".equals(schemaType)) {
            String ref = schema.getItems().get$ref();
            String schemapart = ref!=null?RefUtils.toSchemaPart(ref):name;
            return "List<" + toClassName(schemapart) + ">";
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
