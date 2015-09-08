package org.oss.jstub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class TestUtil {
    public static void assertAllFieldsMatch(Object object, Predicate<Object> predicate) {
        List<String> problematicFields = new ArrayList<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object o = field.get(object);
                if (!predicate.test(o)) {
                    String fieldName = field.getName();
                    problematicFields.add(fieldName);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        if (!problematicFields.isEmpty()) {
            throw new AssertionError("Fields do not match predicate " + problematicFields);
        }
    }

    public static void assertAllFieldsNotNull(Object object) {
        assertAllFieldsMatch(object, o -> o != null);
    }

    public static <T> void assertFieldsPredicate(T o1, T o2,
                                                 BiPredicate<Object, Object> pred) {
        List<String> problematicFields = new ArrayList<>();

        for (Field field : o1.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object val1 = field.get(o1);
                Object val2 = field.get(o2);
                if (!pred.test(val1, val2)) {
                    String fieldName = field.getName();
                    problematicFields.add(fieldName + " -  " + val1 + " =/= " + val2 + "\n");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        if (!problematicFields.isEmpty()) {
            throw new AssertionError("Mismatch for values " + problematicFields);
        }
    }

    public static <T> void assertFieldsEquals(T o1, T o2) {
        assertFieldsPredicate(o1, o2, (ob1, ob2) -> ob1 != null && ob1.equals(ob2));
    }

    public static <T> void assertFieldsNotEquals(T o1, T o2) {
        assertFieldsPredicate(o1, o2, (ob1, ob2) -> ob1 != null && !ob1.equals(ob2));
    }

}
