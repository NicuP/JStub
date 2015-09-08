package org.oss.jstub;

import org.junit.Test;
import org.oss.jstub.pojo.BasicEnum;
import org.oss.jstub.pojo.BasicInterface;
import org.oss.jstub.pojo.FullPojo;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.oss.jstub.TestUtil.assertAllFieldsNotNull;
import static org.oss.jstub.TestUtil.assertFieldsNotEquals;

public class StubFactoryTest {

    public static final StubFactory STUB_FACTORY = StubFactory.instance();

    @Test
    public void testFull() {
        FullPojo fullPojo = STUB_FACTORY.createStub(FullPojo.class);
        assertAllFieldsNotNull(fullPojo);
    }

    @Test
    /*This might fail*/
    public void testRandomValuesAtDistinctCalls() {
        FullPojo fullPojo1 = STUB_FACTORY.createStub(FullPojo.class);
        FullPojo fullPojo2 = STUB_FACTORY.createStub(FullPojo.class);
        assertFieldsNotEquals(fullPojo1, fullPojo2);
    }

    @Test
    public void testCreateList() {
        int collectionsSize = 5;
        List<FullPojo> pojos = STUB_FACTORY.createStubCollection(collectionsSize,
                ArrayList::new, FullPojo.class);
        pojos.forEach(TestUtil::assertAllFieldsNotNull);
        assertEquals(collectionsSize, pojos.size());

        @SuppressWarnings("unchecked")
        List<FullPojo> pojos2 = STUB_FACTORY.createStub(List.class, FullPojo.class);
        pojos2.forEach(TestUtil::assertAllFieldsNotNull);
        assertEquals(StubFactory.DEFAULT_COLLECTION_SIZE, pojos2.size());
    }

    @Test
    public void testCreateSet() {
        int collectionsSize = 5;
        Set<FullPojo> pojos = STUB_FACTORY.createStubCollection(collectionsSize,
                HashSet::new, FullPojo.class);
        pojos.forEach(TestUtil::assertAllFieldsNotNull);
        assertEquals(collectionsSize, pojos.size());

        @SuppressWarnings("unchecked")
        Set<FullPojo> pojos2 = STUB_FACTORY.createStub(Set.class, FullPojo.class);
        pojos2.forEach(TestUtil::assertAllFieldsNotNull);
        assertEquals(StubFactory.DEFAULT_COLLECTION_SIZE, pojos2.size());
    }

    @Test
    public void testCreateMap() {
        @SuppressWarnings("unchecked")
        Map<String, FullPojo> stub = STUB_FACTORY.createStub(Map.class, String.class, FullPojo.class);
        stub.forEach((k, v) -> assertAllFieldsNotNull(v));
    }

    @Test
    public void testCreateEnum() {
        BasicEnum basicEnum = STUB_FACTORY.createStub(BasicEnum.class);
        assertNotNull(basicEnum);
    }

    @Test
    public void testBasicInterface() {
        BasicInterface basicInterface = STUB_FACTORY.createStub(BasicInterface.class);
        assertNotNull(basicInterface);
        assertNotNull(basicInterface.getInt());
        assertNotNull(basicInterface.getSimplePojo());
        assertNotNull(basicInterface.getString());
        assertNotNull(basicInterface.getSelf());
    }
}
