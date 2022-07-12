package org.test.codegen.app.utils;

import io.swagger.v3.oas.models.Components;

public class RefUtils {
    public static String toSchemaPart(String ref) {
        return ref.substring(ref.lastIndexOf(Components.COMPONENTS_SCHEMAS_REF) + Components.COMPONENTS_SCHEMAS_REF.length());
    }
}
