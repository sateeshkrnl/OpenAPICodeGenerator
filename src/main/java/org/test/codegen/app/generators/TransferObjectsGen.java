package org.test.codegen.app.generators;

import io.swagger.v3.oas.models.media.Schema;
import jakarta.enterprise.inject.Vetoed;
import org.test.codegen.app.metadata.BeanClassMetaData;
import org.test.codegen.app.metadata.PropsMetaData;
import org.test.codegen.app.template.TemplateManager;
import org.test.codegen.app.utils.JavaUtils;
import org.test.codegen.app.utils.RefUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Vetoed
public class TransferObjectsGen {
    private String basePackage;
    private Map<String, BeanClassMetaData> beanClassesMetaData = new HashMap<>();
    private Map<String, Schema> schemas = new HashMap<>();
    private TemplateManager templateManager=new TemplateManager();

    public TransferObjectsGen(Map<String, Schema> schemas, String basePackage) {
        this.schemas = schemas;
        this.basePackage = basePackage;
    }

    public Map<String, BeanClassMetaData> getBeanClassesMetaData() {
        return beanClassesMetaData;
    }

    private BeanClassMetaData buildBeanClass(String className, Schema schema) {
        BeanClassMetaData metaData = new BeanClassMetaData();
        Schema schemaref = schema;
        metaData.setClassName(className);
        if (schema.get$ref() != null) {
            String schemapart = RefUtils.toSchemaPart(schema.get$ref());
            schemaref = this.schemas.get(JavaUtils.toClassName(schemapart));
        }
        metaData.setPackageName(JavaUtils.toPackage(basePackage, "to"));
        schemaref.getProperties().forEach((n, s) -> {
            String name = null;
            Schema schemain = null;
            if (n instanceof String)
                name = (String) n;
            if (s instanceof Schema)
                schemain = (Schema) s;
            String type = JavaUtils.toType(name, schemain);
            if (schemain.getType().equals("object")) {
                beanClassesMetaData.put(type, buildBeanClass(type, schemain));
            }else if(schemain.getType().equals("array")){
                beanClassesMetaData.put(type, buildBeanClass(type, schemain.getItems()));
            }
            metaData.getProperties().add(new PropsMetaData(name, type));
        });
        return metaData;
    }

    public void transform() {
        schemas.forEach((k, v) -> {
            Schema sc = v.getType().equals("array")?v.getItems():v;
            String className= v.getType().equals("array")?JavaUtils.removeList(k):k;
            if(beanClassesMetaData.get(className)==null) {
                beanClassesMetaData.put(className, buildBeanClass(className, sc));
            }
        });
    }

    public void generateFiles(String outputDir){
        beanClassesMetaData.values().forEach(meta->{
            Path filePath = Path.of(outputDir,JavaUtils.toPath(meta.getPackageName(),meta.getClassName()).toString());
            templateManager.processTemplate("beanclasstemplate.ftl",meta,filePath);
        });
    }
}
