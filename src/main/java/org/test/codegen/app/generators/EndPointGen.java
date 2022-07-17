package org.test.codegen.app.generators;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.test.codegen.app.cli.CliOpts;
import org.test.codegen.app.config.ConfigFileParser;
import org.test.codegen.app.metadata.ArgMetaData;
import org.test.codegen.app.metadata.ClassMetaData;
import org.test.codegen.app.metadata.MethodMetaData;
import org.test.codegen.app.metadata.PropsMetaData;
import org.test.codegen.app.template.TemplateManager;
import org.test.codegen.app.utils.JavaUtils;
import org.test.codegen.app.utils.OperationUtils;

import java.nio.file.Path;
import java.util.*;
@Singleton
public class EndPointGen implements IGenerator {
    private String basePackage;
    private List<ClassMetaData> classesMetaData = new ArrayList<>();
    private Map<String, List<Operation>> operations = new HashMap<>();
    @Inject
    private TemplateManager templateManager;

    public List<ClassMetaData> getClassesMetaData() {
        return classesMetaData;
    }

    @Override
    public void initialize(ConfigFileParser parser, String basePackage){
        this.operations = parser.getOperations();
        this.basePackage= basePackage;
    }

    @Override
    public void transform(){
        operations.entrySet().forEach(e->{
            classesMetaData.add(buildClassMetaData(e.getKey(),e.getValue()));
        });
    }

    private ClassMetaData buildClassMetaData(String className, List<Operation> operations) {
        ClassMetaData.Builder builder = new ClassMetaData.Builder();
        builder.className(JavaUtils.toClassName(className+"EndPoint"));
        builder.addAnnotation("Component");
        builder.packageName(JavaUtils.toPackage(this.basePackage, "endpoint"));
        builder.addImport("javax.ws.rs.*");
        builder.addImport(JavaUtils.toPackage(this.basePackage, "to", "*"));
        builder.addProp(buildProp(className));
        operations.forEach(operation -> builder.addMethod(buildMethod(operation)));
        return builder.build();
    }

    private MethodMetaData buildMethod(Operation operation) {
        PathItem.HttpMethod httpMethod = OperationUtils.computeMethod(operation);
        String url = OperationUtils.computeUrl(operation);
        Optional<String> reqContentType = Optional.ofNullable((String)operation.getExtensions().get("x-req-content-type"));
        Optional<String> resContentType = Optional.ofNullable((String)operation.getExtensions().get("x-res-content-type"));
        MethodMetaData.Builder builder = new MethodMetaData.Builder();
        builder.methodName(operation.getOperationId());
        builder.annoation(httpMethod.name());
        builder.annoation(String.format("Path(\"%s\")",url));
        switch (httpMethod) {
            case POST:
            case PUT:
                reqContentType.ifPresent(
                    contentType -> builder.annoation(String.format("Produces(\"%s\")",contentType))
                );
                resContentType.ifPresent(
                    contentType -> builder.annoation(String.format("Consumes(\"%s\")",contentType))
                );
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
        return (String)operation.getExtensions().get("x-res-class-name");
    }

    /*private String buildMethodBody(Operation operation){
        List<String> body = new ArrayList<>();
        operation
    }*/

    private ArgMetaData buildArg(Parameter param) {
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

    private ArgMetaData buildArg(Operation operation, RequestBody requestBody) {
        ArgMetaData metaData = new ArgMetaData();
        metaData.setName(JavaUtils.toPropName((String)operation.getExtensions().get("x-req-class-name")));
        metaData.setType(JavaUtils.toClassName((String)operation.getExtensions().get("x-req-class-name")));
        return metaData;
    }

    private PropsMetaData buildProp(String className){
        PropsMetaData.Builder builder = new PropsMetaData.Builder();
        builder.name(JavaUtils.toPropName(className+"Service"));
        builder.type(JavaUtils.toClassName(className+"Service"));
        builder.addAnnotation("Autowired");
        return builder.build();
    }

    @Override
    public void generateFiles(String outputDir){
        this.classesMetaData.forEach(meta->{
            Path filePath = Path.of(outputDir, JavaUtils.toPath(meta.getPackageName(), meta.getClassName()).toString());
            templateManager.processTemplate("genericclasstemplate.ftl",meta,filePath);
        });
    }
}
