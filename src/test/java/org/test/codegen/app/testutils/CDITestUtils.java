package org.test.codegen.app.testutils;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CDITestUtils {
    public static <T> void cdiConsume(Class<T> type, Consumer<T> consumer){
        SeContainerInitializer intializer = SeContainerInitializer.newInstance();
        try (SeContainer container = intializer.initialize()) {
            T object =  container.select(type).get();
            consumer.accept(object);
        }
    }
}
