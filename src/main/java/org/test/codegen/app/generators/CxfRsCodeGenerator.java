package org.test.codegen.app.generators;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.test.codegen.app.cli.CliOpts;
import org.test.codegen.app.config.ConfigFileParser;

import java.util.List;

@Singleton
public class CxfRsCodeGenerator {
    @Inject
    @CxfGenerators
    private List<IGenerator> generators;

    @Inject
    private ConfigFileParser parser;

    public void generate(CliOpts opts){
        generators.forEach(g->{
            g.initialize(parser,opts.getPackageName());
            g.transform();
            g.generateFiles(opts.getOutputDir());
        });
    }
}
