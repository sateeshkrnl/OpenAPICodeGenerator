package org.test.codegen.app.generators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.test.codegen.app.config.ConfigFileParser;

import static org.junit.jupiter.api.Assertions.*;

class EndPointGenTest {
    private ConfigFileParser parser=new ConfigFileParser();
    @BeforeEach
    void setUp(){
        parser.parse("src/main/resources/exampleopenapi.yml");
    }
    @Test
    void verifyEndPointGen(){
        EndPointGen endPointGen = new EndPointGen(parser.getOperations(), "org.sam.test");
        endPointGen.transform();
        assertFalse(endPointGen.getClassesMetaData().isEmpty());
        endPointGen.generateFiles("/home/sateesh/testcodegenerator12345");
    }
}