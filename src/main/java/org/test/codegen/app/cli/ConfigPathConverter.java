package org.test.codegen.app.cli;

import java.nio.file.Files;
import java.nio.file.Path;

import static picocli.CommandLine.ITypeConverter;
import static picocli.CommandLine.TypeConversionException;

public class ConfigPathConverter implements ITypeConverter<Path> {
    @Override
    public Path convert(String path) throws TypeConversionException {
        Path filePath = Path.of(path);
        if (!Files.exists(filePath)) {
            throw new TypeConversionException(String.format("Unable to open the file %s", path));
        }
        return filePath;
    }
}
