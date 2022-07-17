package org.test.codegen.app.metadata;

import java.util.ArrayList;
import java.util.List;

public class PropsMetaData {
    private String type;
    private String name;
    private String access = "private";
    private List<String> annotations =new ArrayList<>();

    public PropsMetaData(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public PropsMetaData(Builder builder) {
        this.name=builder.name;
        this.type=builder.type;
        this.access=builder.access;
        this.annotations =builder.annotations;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getAccess() {
        return access;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public static class Builder {
        private String type;
        private String name;
        private String access = "private";
        private List<String> annotations = new ArrayList<>();
        public Builder name(String name){
            this.name=name;
            return this;
        }
        public Builder type(String type){
            this.type=type;
            return this;
        }
        public Builder access(String access){
            this.access=access;
            return this;
        }
        public Builder addAnnotation(String annotation){
            annotations.add(annotation);
            return this;
        }
        public PropsMetaData build(){
            return new PropsMetaData(this);
        }

    }
}
