package org.test.codegen.app.utils;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;

import java.util.Arrays;

public class OperationUtils {
    public static String computeKey(String url, PathItem.HttpMethod method){
        return String.format("%s_%s",url,method.name());
    }

    public static String computeUrl(Operation operation){
        return (String)operation.getExtensions().get("x-api-url");
    }

    public static PathItem.HttpMethod computeMethod(Operation operation){
        return (PathItem.HttpMethod) operation.getExtensions().get("x-api-method");
    }

    public static String computeClassName(String url){
        String[] classparts = url.split("/");
        String className = Arrays.stream(classparts).limit(classparts.length-1).reduce("",String::concat);
        return className;
    }
}
