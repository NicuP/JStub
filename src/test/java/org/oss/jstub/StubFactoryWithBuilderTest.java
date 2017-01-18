package org.oss.jstub;

import org.junit.Test;
import org.oss.jstub.pojo.SimplePojo;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StubFactoryWithBuilderTest {

    @Test
    public void testWithIgnoreSetters() {
        StubFactory stubFactory = StubFactory.builder()
                .addIgnoredSetter("setName")
                .build();
        SimplePojo pojo = stubFactory.createStub(SimplePojo.class);
        assertNull(pojo.getName());
        assertTrue(pojo.getAnInt() != 0);
    }

    @Test
    public void testWithCustomValue() {
        String[] customValues = {"a", "b"};
        StubFactory stubFactory = StubFactory.builder()
                .addCustomValue("setName", customValues)
                .build();
        SimplePojo pojo = stubFactory.createStub(SimplePojo.class);
        assertTrue(Arrays.asList(customValues).contains(pojo.getName()));
    }

    @Test
    public void testCustomSupplier() {
        StubFactory stubFactory = StubFactory.builder()
                .addCustomSupplier("setName", this::getValue)
                .build();
        SimplePojo pojo = stubFactory.createStub(SimplePojo.class);
        assertEquals(getValue(), pojo.getName());
    }

    private String getValue() {
        return "I kent bă Liviu";
    }

}
