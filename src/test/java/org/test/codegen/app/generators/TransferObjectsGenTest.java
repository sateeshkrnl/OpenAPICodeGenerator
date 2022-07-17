package org.test.codegen.app.generators;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.test.codegen.app.Main;
import org.test.codegen.app.config.ConfigFileParser;
import org.test.codegen.app.testutils.CDITestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransferObjectsGenTest {
    private ConfigFileParser parser = new ConfigFileParser();

    @BeforeEach
    void setUp() {
        parser.parse("src/main/resources/exampleopenapi.yml");
    }

    @Test
    void testScene1() {
        CDITestUtils.cdiConsume(TransferObjectsGen.class,(gen)->{
            gen.initialize(parser,"org.sample.app");
            gen.transform();
            assertEquals(3, gen.getBeanClassesMetaData().size());
            gen.generateFiles("/home/sateesh/testcodegenerator12345");
        });
    }
}