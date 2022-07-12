package org.test.codegen.app.cli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

class ConfigPathConverterTest {
    private ConfigPathConverter converter = new ConfigPathConverter();

    @Test
    void validPathConvert() {
        AtomicReference<Path> filePath = new AtomicReference<>();
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> {
                    filePath.set(converter.convert("src/test/java/org/test/codegen/app/cli/testconfigfile.yml"));
                }),
                () -> Assertions.assertNotNull(filePath.get())
        );
    }

    @Test
    void invalidPathConvert() {
        AtomicReference<Path> filePath = new AtomicReference<>();
        Assertions.assertAll(
                () -> Assertions.assertThrows(CommandLine.TypeConversionException.class, () -> {
                    filePath.set(converter.convert("src/test/java/org/test/codegen/app/cli/testconfigfileinvalid.yml"));
                }),
                () -> Assertions.assertNull(filePath.get())
        );
    }
}