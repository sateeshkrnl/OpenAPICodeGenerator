package org.test.codegen.app.generators;

import jakarta.inject.Singleton;

@Singleton
public class SpringBootGenerator extends AbstractCodeGenerator {

    @Override
    public void generate() {
        System.out.println("inside spring boot generator");
    }
}
