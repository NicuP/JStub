package org.oss.jstub;

import org.junit.Test;
import org.oss.jstub.pojo.BasicEnum;
import org.oss.jstub.pojo.BasicInterface;
import org.oss.jstub.pojo.FullPojo;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.oss.jstub.TestUtil.assertAllFieldsNotNull;

public class StubFactoryBasicTest {

    public static final StubFactory stubFactory = StubFactory.get();

    @Test
    public void testFull() {
        FullPojo fullPojo = stubFactory.createStub(FullPojo.class);
        assertAllFieldsNotNull(fullPojo);
    }

    @Test
    public void testCreateList() {
        int collectionsSize = 5;
        List<FullPojo> pojos = stubFactory.createStubCollection(collectionsSize,
                ArrayList::new, FullPojo.class);
        pojos.forEach(TestUtil::assertAllFieldsNotNull);
        assertEquals(collectionsSize, pojos.size());

        @SuppressWarnings("unchecked")
        List<FullPojo> pojos2 = stubFactory.createStub(List.class, FullPojo.class);
        pojos2.forEach(TestUtil::assertAllFieldsNotNull);
        assertEquals(StubFactory.DEFAULT_COLLECTION_SIZE, pojos2.size());
    }

    @Test
    public void testCreateSet() {
        int collectionsSize = 5;
        Set<FullPojo> pojos = stubFactory.createStubCollection(collectionsSize,
                HashSet::new, FullPojo.class);
        pojos.forEach(TestUtil::assertAllFieldsNotNull);
        assertEquals(collectionsSize, pojos.size());

        @SuppressWarnings("unchecked")
        Set<FullPojo> pojos2 = stubFactory.createStub(Set.class, FullPojo.class);
        pojos2.forEach(TestUtil::assertAllFieldsNotNull);
        assertEquals(StubFactory.DEFAULT_COLLECTION_SIZE, pojos2.size());
    }

    @Test
    public void testCreateMap() {
        @SuppressWarnings("unchecked")
        Map<String, FullPojo> stub = stubFactory.createStub(Map.class, String.class, FullPojo.class);
        stub.forEach((k, v) -> assertAllFieldsNotNull(v));
    }

    @Test
    public void testCreateEnum() {
        BasicEnum basicEnum = stubFactory.createStub(BasicEnum.class);
        assertNotNull(basicEnum);
    }

    @Test
    public void testBasicInterface() {
        BasicInterface basicInterface = stubFactory.createStub(BasicInterface.class);
        assertNotNull(basicInterface);
        assertNotNull(basicInterface.getInt());
        assertNotNull(basicInterface.getSimplePojo());
        assertNotNull(basicInterface.getString());
        assertNotNull(basicInterface.getSelf());
    }

    @Test
    public void testCollectionSize() {
        int collectionSize = 42;
        StubFactory stubFactory = StubFactory.get(collectionSize);
        FullPojo pojo = stubFactory.createStub(FullPojo.class);
        assertEquals(collectionSize, pojo.getStringList().size());
        assertEquals(collectionSize, pojo.getMap().size());
    }
}
