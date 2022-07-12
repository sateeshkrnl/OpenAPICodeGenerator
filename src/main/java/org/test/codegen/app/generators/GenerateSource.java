package org.test.codegen.app.generators;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class GenerateSource {
    @Inject
    private SpringBootGenerator bootGenerator;

    public void generate() {
        bootGenerator.codeGenerate();
    }
}
