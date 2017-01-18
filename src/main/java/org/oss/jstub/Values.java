package org.oss.jstub;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * The values for the fields are generated here
 *
 * @author nicu
 */
final class Values {
    private final RandomGenerator randomGenerator;
    private final Map<String, Object[]> customValues;
    private final Map<String, Supplier<?>> customSuppliers;
    private final Map<Class<?>, Supplier<?>> defaultValues;
    private final Set<String> ignoredSetters;

    Values() {
        randomGenerator = new RandomGenerator();
        defaultValues = defaultValuesMap();
        customValues = Collections.emptyMap();
        customSuppliers = Collections.emptyMap();
        ignoredSetters = Collections.emptySet();
    }

    Values(Map<String, Object[]> customValues, Map<String, Supplier<?>> customSuppliers,
           Set<String> ignoredSetters) {
        randomGenerator = new RandomGenerator();
        defaultValues = defaultValuesMap();
        this.customValues = customValues;
        this.customSuppliers = customSuppliers;
        this.ignoredSetters = ignoredSetters;
    }

    private Map<Class<?>, Supplier<?>> defaultValuesMap() {
        Map<Class<?>, Supplier<?>> supplierMap = new HashMap<>();
        Class<RandomGenerator> clz = RandomGenerator.class;
        for (Method method : clz.getMethods()) {
            if (method.getName().startsWith("next") && method.getParameterCount() == 0) {
                Class<?> returnType = method.getReturnType();
                Supplier<?> supplier = safeSupplier(method, randomGenerator, "Cannot create default values");
                supplierMap.putIfAbsent(returnType, supplier);
            }
        }
        return supplierMap;
    }

    private Supplier<?> safeSupplier(Method method, Object invocation, String message) {
        return () -> {
            try {
                return method.invoke(invocation);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(message, e);
            }
        };
    }

    RandomGenerator getRandomGenerator() {
        return randomGenerator;
    }

    Object randomDefaultValue(Class<?> clz) {
        return defaultValues.get(clz).get();
    }

    Object getCustomValue(String methodName) {
        if (customSuppliers.containsKey(methodName)) {
            return customSuppliers.get(methodName).get();
        }
        Object[] values = customValues.get(methodName);
        return values[randomGenerator.nextInt(values.length)];
    }

    boolean isDefaultValueType(Class<?> parameterType) {
        return defaultValues.containsKey(parameterType);
    }

    boolean isCustomValue(String methodName) {
        return customValues.containsKey(methodName) ||
                customSuppliers.containsKey(methodName);
    }

    boolean isIgnoredSetter(String setterName) {
        return ignoredSetters.contains(setterName);
    }
}
