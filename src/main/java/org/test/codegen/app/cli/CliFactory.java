package org.test.codegen.app.cli;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import picocli.CommandLine;

import static picocli.CommandLine.IFactory;

@ApplicationScoped
public class CliFactory implements IFactory {
    private final IFactory defaultFactory = CommandLine.defaultFactory();

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        Instance<K> instance = CDI.current().select(cls);
        if (instance.isResolvable()) {
            return instance.get();
        }
        return defaultFactory.create(cls);
    }
}
