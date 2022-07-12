package org.test.codegen.app;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.codegen.app.cli.CliFactory;
import org.test.codegen.app.cli.GenerateCommand;
import picocli.CommandLine;

@Singleton
public class Main {
    @Inject
    private static Logger log;
    @Inject
    private CliFactory cliFactory;

    public static void main(String[] args) {
        SeContainerInitializer intializer = SeContainerInitializer.newInstance();
        try (SeContainer container = intializer.initialize()) {
            container.select(Main.class).get().run(args);
        }
    }

    private void run(String[] args) {
        new CommandLine(GenerateCommand.class, cliFactory).execute(args);
    }

    @Produces
    public Logger logger(InjectionPoint ip) {
        return LoggerFactory.getLogger(ip.getMember().getDeclaringClass().getClass());
    }
}
