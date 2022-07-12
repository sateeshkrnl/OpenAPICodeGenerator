package org.test.codegen.app.generators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.test.codegen.app.config.ConfigFileParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransferObjectsGenTest {
    private ConfigFileParser parser = new ConfigFileParser();

    @BeforeEach
    void setUp() {
        parser.parse("src/main/resources/exampleopenapi.yml");
    }

    @Test
    void testScene1() {
        TransferObjectsGen gen = new TransferObjectsGen(parser.getSchemas(), "org.sam.app");
        gen.transform();
        assertEquals(3, gen.getBeanClassesMetaData().size());
    }
}