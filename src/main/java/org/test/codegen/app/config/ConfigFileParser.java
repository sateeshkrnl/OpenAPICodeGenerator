package org.test.codegen.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.test.codegen.app.exception.AppException;
import org.test.codegen.app.utils.JavaUtils;
import org.test.codegen.app.utils.OperationUtils;
import org.test.codegen.app.utils.RefUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class ConfigFileParser {
    @Inject
    private static Logger log;

    private OpenAPIV3Parser parser = new OpenAPIV3Parser();
    private OpenAPI api;
    private Map<String, Schema> schemas = new HashMap<>();
    private Map<String, Parameter> parameters = new HashMap<>();
    private Map<String, List<Operation>> operations = new HashMap<>();
    private Map<String, RequestBody> requestBodies = new HashMap<>();
    private Map<String, ApiResponse> responses = new HashMap<>();

    public Map<String, Schema> getSchemas() {
        return schemas;
    }

    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Parameter> parameters) {
        this.parameters = parameters;
    }

    public Map<String, List<Operation>> getOperations() {
        return operations;
    }

    public Map<String, RequestBody> getRequestBodies() {
        return requestBodies;
    }

    public Map<String, ApiResponse> getResponses() {
        return responses;
    }

    public void parse(String configFile) {
        SwaggerParseResult result = parser.readLocation(configFile, null, null);
        if (result.getMessages().isEmpty()) {
            api = result.getOpenAPI();
            preprocess(api);
        } else {
            log.error(String.format("invalid open api file %s",result.getMessages()));
            throw new AppException("invalid open api file");
        }
    }

    public void parse(Path path){
        parse(path.toString());
    }

    private void preprocess(OpenAPI api) {
        handleComponents(api);
        if (api.getPaths() != null) {
            api.getPaths().forEach((url, pathItem) ->{
                String className = OperationUtils.computeClassName(url);
                pathItem.readOperationsMap().forEach((method, operation) -> {
                    String operationId = operation.getOperationId();
                    operation.addExtension("x-api-method", method);
                    operation.addExtension("x-api-url", url);
                    operations.merge(className,Arrays.asList(operation),(k,o)->{
                       o.add(operation);
                       return o;
                    });
                    handleRequestBody(operation, operationId);
                    handleResponses(operation, operationId);
                });
            });
        }
    }

    private void handleResponses(Operation operation, String operationId) {
        if (operation.getResponses() != null) {
            operation.getResponses().forEach((statusCode, response) ->
                response.getContent().forEach((contentType, mediaType) -> {
                    operation.addExtension("x-res-content-type",contentType);
                    if (mediaType.getSchema().get$ref() == null) {
                        String respClassName = JavaUtils.toType(operationId + "ResTo", mediaType.getSchema());
                        schemas.put(respClassName, mediaType.getSchema());
                        operation.addExtension("x-res-class-name", respClassName);
                    } else {
                        String ref = mediaType.getSchema().get$ref();
                        String respClassName = RefUtils.toSchemaPart(ref);
                        operation.addExtension("x-res-class-name", JavaUtils.toClassName(respClassName));
                    }
                })
            );
        }
    }

    private void handleRequestBody(Operation operation, String operationId) {
        RequestBody requestBody = operation.getRequestBody();
        if (requestBody != null && requestBody.getContent() != null) {
            requestBody.getContent().forEach((contentType, mediaType) -> {
                operation.addExtension("x-req-content-type",contentType);
                if (mediaType.getSchema().get$ref() == null) {
                    String reqClassName = JavaUtils.toType(operationId + "ResTo", mediaType.getSchema());
                    schemas.put(reqClassName, mediaType.getSchema());
                    operation.addExtension("x-req-class-name", reqClassName);
                } else {
                    String ref = mediaType.getSchema().get$ref();
                    String reqClassName = RefUtils.toSchemaPart(ref);
                    operation.addExtension("x-req-class-name", JavaUtils.toClassName(reqClassName));
                }
            });
        }
    }

    private void handleComponents(OpenAPI api) {
        if (api.getComponents() != null && api.getComponents().getSchemas() != null) {
            schemas = api.getComponents().getSchemas().entrySet().stream().filter(e->e.getValue().get$ref()==null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        if (api.getComponents() != null && api.getComponents().getParameters() != null) {
            parameters = api.getComponents().getParameters().entrySet().stream().filter(e->e.getValue().get$ref()==null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        if (api.getComponents() != null && api.getComponents().getRequestBodies() != null) {
            requestBodies = api.getComponents().getRequestBodies().entrySet().stream().filter(e->e.getValue().get$ref()==null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        if (api.getComponents() != null && api.getComponents().getResponses() != null) {
            responses = api.getComponents().getResponses().entrySet().stream().filter(e->e.getValue().get$ref()==null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }

}
