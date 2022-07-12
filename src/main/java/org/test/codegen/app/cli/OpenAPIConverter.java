package org.test.codegen.app.cli;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.test.codegen.app.exception.AppException;
import picocli.CommandLine;

@Singleton
public class OpenAPIConverter implements CommandLine.ITypeConverter<OpenAPI> {
    @Inject
    private static Logger log;

    private OpenAPIV3Parser parser = new OpenAPIV3Parser();

    @Override
    public OpenAPI convert(String filePath) {
        SwaggerParseResult result = parser.readLocation(filePath, null, null);
        if (result.getMessages().isEmpty())
            return result.getOpenAPI();
        else {
            log.error("invalid open api file " + result.getMessages());
            throw new AppException("invalid open api file");
        }
    }
}
