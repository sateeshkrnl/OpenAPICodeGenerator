package org.test.codegen.app.utils;

import io.swagger.v3.oas.models.PathItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperationUtilsTest {
    @Test
    void testComputeKey(){
        String key = OperationUtils.computeKey("/Cards/Search", PathItem.HttpMethod.POST);
        assertEquals("/Cards/Search_POST",key);
    }
}