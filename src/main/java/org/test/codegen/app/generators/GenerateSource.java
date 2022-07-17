package org.test.codegen.app.generators;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.test.codegen.app.cli.CliOpts;

@Singleton
public class GenerateSource {
    @Inject
    private SpringBootGenerator bootGenerator;
    @Inject
    private CxfRsCodeGenerator cxfGenerators;
    public void generate(CliOpts cliOpts) {
        //bootGenerator.codeGenerate();
        cxfGenerators.generate(cliOpts);
    }
}
