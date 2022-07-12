package org.test.codegen.app.metadata;

import java.util.ArrayList;
import java.util.List;

public class BeanClassMetaData {
    private String className;
    private String packageName;
    private List<PropsMetaData> properties = new ArrayList<>();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<PropsMetaData> getProperties() {
        return properties;
    }

    public void setProperties(List<PropsMetaData> properties) {
        this.properties = properties;
    }
}
