package org.test.codegen.app.cli;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.test.codegen.app.config.ConfigFileParser;
import org.test.codegen.app.generators.GenerateSource;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Mixin;

@Singleton
@Command(mixinStandardHelpOptions = true)
public class GenerateCommand implements Runnable {
    @Mixin
    @Inject
    private CliOpts cliOpts;

    @Inject
    private ConfigFileParser parser;

    @Inject
    private GenerateSource generateSource;

    @Override
    public void run() {
        System.out.println("hello in picocli command");
        parser.parse(cliOpts.getOpenAPI());
        generateSource.generate(cliOpts);
    }
}
