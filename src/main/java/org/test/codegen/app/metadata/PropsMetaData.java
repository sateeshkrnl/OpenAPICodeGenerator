package org.test.codegen.app.metadata;

public class PropsMetaData {
    private String type;
    private String name;
    private String access = "private";

    public PropsMetaData(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public static class Builder {
        private String type;
        private String name;
        private String access = "private";
    }
}
