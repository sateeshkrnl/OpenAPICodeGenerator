package org.test.codegen.app.metadata;

import java.util.ArrayList;
import java.util.List;

public class ClassMetaData {
    private String packageName;
    private List<String> imports;
    private String className;
    private List<PropsMetaData> properties;
    private List<MethodMetaData> methods;
    private boolean isInterface=false;
    private List<String> annotations = new ArrayList<>();

    private ClassMetaData(Builder builder) {
        this.className = builder.className;
        this.packageName = builder.packageName;
        this.imports = builder.imports;
        this.properties = builder.properties;
        this.methods = builder.methods;
        this.isInterface=builder.isInterface;
        this.annotations=builder.annotations;
    }

    public String getPackageName() {
        return packageName;
    }

    public List<String> getImports() {
        return imports;
    }

    public String getClassName() {
        return className;
    }

    public List<PropsMetaData> getProperties() {
        return properties;
    }

    public List<MethodMetaData> getMethods() {
        return methods;
    }

    public boolean getIsInterface() {
        return isInterface;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public static class Builder {
        private String packageName;
        private List<String> imports = new ArrayList<>();
        private String className;
        private List<PropsMetaData> properties = new ArrayList<>();
        private List<MethodMetaData> methods = new ArrayList<>();
        private boolean isInterface =false;
        private List<String> annotations = new ArrayList<>();

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder imports(List<String> imports) {
            this.imports = imports;
            return this;
        }

        public Builder addImport(String importStr) {
            imports.add(importStr);
            return this;
        }

        public Builder addProp(PropsMetaData propsMetaData) {
            properties.add(propsMetaData);
            return this;
        }

        public Builder addProp(String name, String type) {
            properties.add(new PropsMetaData(name, type));
            return this;
        }

        public Builder addMethod(MethodMetaData methodMetaData) {
            methods.add(methodMetaData);
            return this;
        }

        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder isInterface(){
            this.isInterface=true;
            return this;
        }

        public Builder isClass(){
            this.isInterface=false;
            return this;
        }

        public Builder addAnnotation(String annotation){
            this.annotations.add(annotation);
            return this;
        }

        public ClassMetaData build() {
            return new ClassMetaData(this);
        }
    }
}
