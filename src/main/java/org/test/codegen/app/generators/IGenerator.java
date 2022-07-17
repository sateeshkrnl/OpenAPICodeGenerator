package org.test.codegen.app.generators;

import org.test.codegen.app.config.ConfigFileParser;

public interface IGenerator {
    void initialize(ConfigFileParser parser, String basePackage);

    void transform();

    void generateFiles(String outputDir);
}
