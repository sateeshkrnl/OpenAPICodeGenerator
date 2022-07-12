package org.test.codegen.app.cli;

import jakarta.inject.Singleton;

import java.nio.file.Path;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Singleton
@Command
public class CliOpts {
    @Option(names = {"-a"}, required = true)
    private String appName;

    @Option(names = {"-o"}, defaultValue = "outgenerated")
    private String outputDir;

    @Option(names = {"-p"}, defaultValue = "org.sample.test")
    private String packageName;

    @Option(names = {"-i"}, required = true, converter = ConfigPathConverter.class)
    private Path openAPI;

    public String getAppName() {
        return appName;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public String getPackageName() {
        return packageName;
    }

    public Path getOpenAPI() {
        return openAPI;
    }
}
