package org.test.codegen.app.metadata;

import java.util.ArrayList;
import java.util.List;

public class MethodMetaData {
    private final String name;
    private String returnType;
    private List<ArgMetaData> arguments = new ArrayList<>();
    private String body = "";
    private String access = "private";
    private List<String> annotations = new ArrayList<>();

    private MethodMetaData(Builder builder) {
        this.name = builder.name;
        this.arguments = builder.arguments;
        this.returnType = builder.returnType;
        this.body = builder.body;
        this.access = builder.access;
        this.annotations = builder.annotations;
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<ArgMetaData> getArguments() {
        return arguments;
    }

    public String getBody() {
        return body;
    }

    public String getAccess() {
        return access;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public static class Builder {
        public String access = "public";
        private String name;
        private String returnType;
        private List<ArgMetaData> arguments = new ArrayList<>();
        private String body = "";
        private List<String> annotations = new ArrayList<>();

        public Builder methodName(String name) {
            this.name = name;
            return this;
        }

        public Builder returnType(String returnType) {
            this.returnType = returnType;
            return this;
        }

        Builder argument(String returnType, String name) {
            this.arguments.add(new ArgMetaData(returnType, name));
            return this;
        }

        public Builder argument(ArgMetaData argument) {
            this.arguments.add(argument);
            return this;
        }

        public Builder arguments(List<ArgMetaData> arguments) {
            this.arguments = arguments;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder access(String access) {
            this.access = access;
            return this;
        }

        public MethodMetaData build() {
            MethodMetaData meta = new MethodMetaData(this);
            return meta;
        }

        public Builder annoation(String annotation) {
            this.annotations.add(annotation);
            return this;
        }
    }
}
