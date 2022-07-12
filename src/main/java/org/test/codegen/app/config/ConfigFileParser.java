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
import org.slf4j.Logger;
import org.test.codegen.app.exception.AppException;
import org.test.codegen.app.utils.JavaUtils;
import org.test.codegen.app.utils.RefUtils;

import java.util.HashMap;
import java.util.Map;

public class ConfigFileParser {
    @Inject
    private static Logger log;

    private OpenAPIV3Parser parser = new OpenAPIV3Parser();
    private OpenAPI api;
    private Map<String, Schema> schemas = new HashMap<>();
    private Map<String, Parameter> parameters = new HashMap<>();
    private Map<String, Operation> operations = new HashMap<>();
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

    public Map<String, Operation> getOperations() {
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
            log.error("invalid open api file " + result.getMessages());
            throw new AppException("invalid open api file");
        }
    }

    private void preprocess(OpenAPI api) {
        if (api.getComponents() != null && api.getComponents().getSchemas() != null) {
            api.getComponents().getSchemas().forEach((k, v) -> {
                if (v.get$ref() == null) {
                    schemas.put(JavaUtils.toClassName(k), v);
                }
            });
        }
        if (api.getComponents() != null && api.getComponents().getParameters() != null) {
            api.getComponents().getParameters().forEach((k, v) -> {
                if (v.get$ref() == null) {
                    parameters.put(k, v);
                }
            });
        }
        if (api.getComponents() != null && api.getComponents().getRequestBodies() != null) {
            api.getComponents().getRequestBodies().forEach((k, v) -> {
                if (v.get$ref() == null) {
                    requestBodies.put(k, v);
                }
            });
        }
        if (api.getComponents() != null && api.getComponents().getResponses() != null) {
            api.getComponents().getResponses().forEach((k, v) -> {
                if (v.get$ref() == null) {
                    responses.put(k, v);
                }
            });
        }
        if (api.getPaths() != null) {
            api.getPaths().forEach((url, pathItem) -> {
                pathItem.readOperationsMap().forEach((method, operation) -> {
                    String operationId = operation.getOperationId();
                    operation.addExtension("x-api-method", method);
                    operations.put(url + "_" + method.name(), operation);
                    if (operation.getRequestBody() != null && operation.getRequestBody().getContent() != null) {
                        operation.getRequestBody().getContent().forEach((contentType, mediaType) -> {
                            if (mediaType.getSchema().get$ref() == null) {
                                String reqClassName = JavaUtils.toType(operationId + "ResTo", mediaType.getSchema().getType());
                                schemas.put(reqClassName, mediaType.getSchema());
                                operation.addExtension("x-req-class-name", reqClassName);
                            } else {
                                String ref = mediaType.getSchema().get$ref();
                                String reqClassName = RefUtils.toSchemaPart(ref);
                                ;
                                operation.addExtension("x-req-class-name", JavaUtils.toClassName(reqClassName));
                            }
                        });
                    }
                    if (operation.getResponses() != null) {
                        operation.getResponses().forEach((statusCode, response) -> {
                            response.getContent().forEach((contentType, mediaType) -> {
                                if (mediaType.getSchema().getType().equals("array")) {
                                    if (mediaType.getSchema().getItems().get$ref() == null) {
                                        String respClassName = JavaUtils.toType(operationId + "ResTo", mediaType.getSchema().getType());
                                        schemas.put(respClassName, mediaType.getSchema());
                                        operation.addExtension("x-res-class-name", respClassName);
                                    } else {
                                        String ref = mediaType.getSchema().getItems().get$ref();
                                        String respClassName = RefUtils.toSchemaPart(ref);
                                        operation.addExtension("x-res-class-name", JavaUtils.toListClassName(respClassName));
                                    }
                                } else if (mediaType.getSchema().get$ref() == null) {
                                    String respClassName = JavaUtils.toType(operationId + "ResTo", mediaType.getSchema().getType());
                                    schemas.put(respClassName, mediaType.getSchema());
                                    operation.addExtension("x-res-class-name", respClassName);
                                } else {
                                    String ref = mediaType.getSchema().get$ref();
                                    String respClassName = RefUtils.toSchemaPart(ref);
                                    operation.addExtension("x-res-class-name", JavaUtils.toClassName(respClassName));
                                }
                            });
                        });
                    }
                });
            });
        }
    }

}
