package org.test.codegen.app.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigFileParserTest {
    private ConfigFileParser parser = new ConfigFileParser();

    @Test
    void testExampleApi() {
        parser.parse("src/main/resources/exampleopenapi.yml");
        assertEquals(3, parser.getSchemas().size());
        assertEquals(1, parser.getOperations().size());
        assertTrue(parser.getParameters().isEmpty());
        assertTrue(parser.getRequestBodies().isEmpty());
        assertTrue(parser.getResponses().isEmpty());
    }
}