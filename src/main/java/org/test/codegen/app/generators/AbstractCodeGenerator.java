package org.test.codegen.app.generators;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.inject.Inject;
import org.test.codegen.app.cli.CliOpts;
import org.test.codegen.app.exception.AppException;
import org.test.codegen.app.metadata.*;
import org.test.codegen.app.template.TemplateManager;
import org.test.codegen.app.utils.JavaUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractCodeGenerator {
    @Inject
    private CliOpts cliOpts;
    @Inject
    private TemplateManager manager;
    private List<BeanClassMetaData> beanClassesMetaData = new ArrayList<>();
    private Map<String, BeanClassMetaData> tos = new HashMap<>();
    private OpenAPI api;

    public void codeGenerate() throws AppException {
        ClassMetaData metaData = buildClassMetaData(this.api);
        tos.putAll(buildCommonBeanMetaData(this.api.getComponents().getSchemas()));
        Path filePath = Path.of(cliOpts.getOutputDir(), JavaUtils.toPath(metaData.getPackageName(), metaData.getClassName()).toString());
        manager.processTemplate("genericclasstemplate.ftl", metaData, filePath);
        beanClassesMetaData.forEach(beanMeta -> {
            Path beanFilePath = Path.of(cliOpts.getOutputDir(), JavaUtils.toPath(beanMeta.getPackageName(), beanMeta.getClassName()).toString());
            manager.processTemplate("beanclasstemplate.ftl", beanMeta, beanFilePath);
        });
    }

    private Map<String, BeanClassMetaData> buildCommonBeanMetaData(Map<String, Schema> schemas) {
        Map<String, BeanClassMetaData> tos = new HashMap<>();
        schemas.forEach((n, s) -> {
            String className = JavaUtils.toClassName(n);
            tos.put(className, buildBeanClass(className, s));
        });
        return tos;
    }

    protected ClassMetaData buildClassMetaData(OpenAPI api) {
        ClassMetaData.Builder builder = new ClassMetaData.Builder();
        builder.className(JavaUtils.toClassName(api.getInfo().getTitle()));
        builder.packageName(JavaUtils.toPackage(cliOpts.getPackageName(), "endpoint"));
        builder.addImport("javax.ws.rs.*");
        builder.addImport(JavaUtils.toPackage(cliOpts.getPackageName(), "to", "*"));
        api.getPaths().forEach((url, pathItem) -> {
            pathItem.readOperationsMap().forEach(
                    (httpMethod, operation) -> {
                        builder.addMethod(buildMethods(httpMethod, operation));
                    }
            );
        });
        return builder.build();
    }

    protected MethodMetaData buildMethods(PathItem.HttpMethod httpMethod, Operation operation) {
        MethodMetaData.Builder builder = new MethodMetaData.Builder();
        builder.methodName(operation.getOperationId());
        builder.annoation(httpMethod.name());
        switch (httpMethod) {
            case POST:
            case PUT:
                builder.annoation("Produces(\"application\\json\")");
                builder.annoation("Consumes(\"application\\json\")");
                break;
            default:
        }
        if (operation.getParameters() != null) {
            operation.getParameters().forEach(p -> {
                builder.argument(buildArg(p));
            });
        }
        if (operation.getRequestBody() != null) {
            builder.argument(buildArg(operation, operation.getRequestBody()));
        }
        if (operation.getResponses() != null) {
            ApiResponse response = operation.getResponses().get("200");
            builder.returnType(buildReturnType(operation, response));
        }
        return builder.build();
    }

    private String buildReturnType(Operation operation, ApiResponse response) {
        AtomicReference<String> returnType = new AtomicReference<>();
        response.getContent().forEach((contentType, mediaType) -> {
            returnType.set(buildReturnType(operation, mediaType));
        });
        return returnType.get();
    }

    private String buildReturnType(Operation operation, MediaType mediaType) {
        Schema schema = mediaType.getSchema();
        String schemapart = operation.getOperationId() + "To";
        if (schema.get$ref() != null) {
            schemapart = schema.get$ref().substring(schema.get$ref().lastIndexOf("/") + 1);
            schema = this.api.getComponents().getSchemas().get(schemapart);
        }
        String returnType = JavaUtils.toType(schemapart, schema);
        if (schema.getType().equals("array")) {
            beanClassesMetaData.add(buildBeanClass(JavaUtils.removeList(returnType), schema.getItems()));
        }
        if (schema.getType().equals("object")) {
            beanClassesMetaData.add(buildBeanClass(returnType, schema));
        }
        return returnType;
    }

    private BeanClassMetaData buildBeanClass(String className, Schema schema) {
        BeanClassMetaData metaData = new BeanClassMetaData();
        Schema schemaref = schema;
        metaData.setClassName(className);
        if (schema.get$ref() != null) {
            String schemapart = schema.get$ref().substring(schema.get$ref().lastIndexOf("/") + 1);
            schemaref = this.api.getComponents().getSchemas().get(schemapart);
        }
        metaData.setPackageName(JavaUtils.toPackage(cliOpts.getPackageName(), "to"));
        schemaref.getProperties().forEach((n, s) -> {
            String name = null;
            Schema schemain = null;
            if (n instanceof String)
                name = (String) n;
            if (s instanceof Schema)
                schemain = (Schema) s;
            String type = JavaUtils.toType(name, schemain);
            if (schemain.getType().equals("object")) {
                beanClassesMetaData.add(buildBeanClass(type, schemain));
            }
            metaData.getProperties().add(new PropsMetaData(name, type));
        });
        return metaData;
    }

    protected ArgMetaData buildArg(Parameter param) {
        ArgMetaData argMetaData = new ArgMetaData();
        argMetaData.setName(param.getName());
        Schema schema = param.getSchema();
        String type = schema.getType();
        if ("string".equals(type)) {
            argMetaData.setType("String");
        } else if ("integer".equals(type)) {
            argMetaData.setType("int");
        } else
            argMetaData.setType("String");
        return argMetaData;
    }

    protected ArgMetaData buildArg(Operation operation, RequestBody requestBody) {
        AtomicReference<ArgMetaData> argMetaData = new AtomicReference<>();
        requestBody.getContent().forEach((contentType, mediaType) -> {
            argMetaData.set(buildArg(operation, mediaType));
        });
        return argMetaData.get();
    }

    protected ArgMetaData buildArg(Operation operation, MediaType mediaType) {
        ArgMetaData argMetaData = new ArgMetaData();
        Schema schema = mediaType.getSchema();
        String schemapart = operation.getOperationId() + "To";
        if (schema.get$ref() != null) {
            schemapart = schema.get$ref().substring(schema.get$ref().lastIndexOf("/") + 1);
            schema = this.api.getComponents().getSchemas().get(schemapart);
        }
        argMetaData.setName(JavaUtils.toPropName(schemapart));
        argMetaData.setType(JavaUtils.toClassName(schemapart));
        beanClassesMetaData.add(buildBeanClass(argMetaData.getType(), schema));
        return argMetaData;
    }

    public abstract void generate();
}
