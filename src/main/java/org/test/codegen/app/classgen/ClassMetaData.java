package org.test.codegen.app.classgen;

import org.test.codegen.app.constants.ClassTypeEnum;

import java.util.ArrayList;
import java.util.List;

public class ClassMetaData {
    private String packageName;
    private String name;
    private List<String> imports;
    private List<String> methods;
    private ClassTypeEnum type;

    private ClassMetaData(Builder builder) {
        this.name = builder.name;
        this.packageName = builder.packageName;
        this.imports = builder.imports;
        this.methods = builder.methods;
        this.type = builder.type;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return name;
    }

    public List<String> getImports() {
        return imports;
    }

    public List<String> getMethods() {
        return methods;
    }

    public ClassTypeEnum getType() {
        return type;
    }

    public static class Builder {
        private String packageName;
        private String name;
        private List<String> imports = new ArrayList<>();
        private List<String> methods = new ArrayList<>();
        private ClassTypeEnum type;

        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder className(String name) {
            this.name = name;
            return this;
        }

        public Builder serviceType() {
            this.type = ClassTypeEnum.SERVICE;
            return this;
        }

        public Builder daoType() {
            this.type = ClassTypeEnum.DAO;
            return this;
        }

        public Builder imports(List<String> imports) {
            this.imports = imports;
            return this;
        }

        public Builder addImport(String importStr) {
            this.imports.add(String.format("%s%n", importStr));
            return this;
        }

        public Builder method(String method) {
            this.methods.add(String.format("%s%n", method));
            return this;
        }

        public Builder methods(List<String> methods) {
            this.methods = methods;
            return this;
        }

        public ClassMetaData build() {
            ClassMetaData codeGen = new ClassMetaData(this);
            return codeGen;
        }
    }
}
