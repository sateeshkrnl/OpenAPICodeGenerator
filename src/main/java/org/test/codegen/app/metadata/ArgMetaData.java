package org.test.codegen.app.metadata;

public class ArgMetaData {
    public String type;
    public String name;

    public ArgMetaData(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public ArgMetaData() {
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }
}
