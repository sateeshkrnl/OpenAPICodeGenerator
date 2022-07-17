package org.test.codegen.app.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.test.codegen.app.classgen.ClassMetaData;
import org.test.codegen.app.constants.AppConstants;
import org.test.codegen.app.metadata.ArgMetaData;
import org.test.codegen.app.metadata.MethodMetaData;

import java.util.concurrent.atomic.AtomicReference;

class TemplateManagerTest {

    private TemplateManager templateManger;

    @BeforeEach
    void setUp() {
        templateManger = new TemplateManager();
    }

    @Test
    @Disabled
    void testClassTemplate() {
        String method = " static void main(String[] args){\n}";
        ClassMetaData.Builder builder = new ClassMetaData.Builder();
        ClassMetaData meta = builder.className("TestClass")
                .packageName("org.codegen.sample")
                .daoType()
                .method(method).build();
        AtomicReference<String> out = new AtomicReference<>();
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> {
                    out.set(templateManger.processTemplate(AppConstants.CLASS_TEMPLATE, meta));
                }),
                () -> Assertions.assertNotNull(out.get()),
                () -> Assertions.assertFalse(out.get().isBlank())
        );
    }

    @Test
    @Disabled
    void testMethodTemplate() {
        MethodMetaData.Builder builder = new MethodMetaData.Builder();
        MethodMetaData metaData = builder.methodName("main")
                .argument(new ArgMetaData("String", "args"))
                .body("").build();
        AtomicReference<String> out = new AtomicReference<>();
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(
                        () -> {
                            out.set(templateManger.processTemplate(AppConstants.METHOD_TEMPLATE, metaData));
                        }
                ),
                () -> Assertions.assertNotNull(out.get()),
                () -> Assertions.assertFalse(out.get().isBlank())
        );
    }

}
