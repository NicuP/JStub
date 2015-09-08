package org.oss.jstub;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Supplier;

final class DefaultValues {
    private final RandomGenerator randomGenerator;
    private final Map<String, Object[]> customValues;
    private final Map<String, Supplier<?>> customSuppliers;
    private final Map<Class<?>, Supplier<?>> defaultValues;
    private final Set<String> ignoredSetters;

    DefaultValues() {
        randomGenerator = new RandomGenerator();
        defaultValues = defaultValuesMap();
        customValues = Collections.emptyMap();
        customSuppliers = Collections.emptyMap();
        ignoredSetters = Collections.emptySet();
    }

    DefaultValues(Map<String, Object[]> customValues, Map<String, Supplier<?>> customSuppliers,
                  Set<String> ignoredSetters) {
        randomGenerator = new RandomGenerator();
        defaultValues = defaultValuesMap();
        this.customValues = customValues;
        this.customSuppliers = customSuppliers;
        this.ignoredSetters = ignoredSetters;
    }

    private Map<Class<?>, Supplier<?>> defaultValuesMap() {
        Map<Class<?>, Supplier<?>> supplierMap = new HashMap<>();
        RandomGenerator random = new RandomGenerator();
        supplierMap.put(String.class, random::nextAlphabeticString);
        supplierMap.put(int.class, random::nextInt);
        supplierMap.put(Integer.class, random::nextInt);
        supplierMap.put(byte.class, random::nextByte);
        supplierMap.put(Byte.class, random::nextByte);
        supplierMap.put(char.class, random::nextChar);
        supplierMap.put(Character.class, random::nextChar);
        supplierMap.put(double.class, random::nextDouble);
        supplierMap.put(Double.class, random::nextDouble);
        supplierMap.put(long.class, random::nextLong);
        supplierMap.put(Long.class, random::nextLong);
        supplierMap.put(float.class, random::nextFloat);
        supplierMap.put(Float.class, random::nextFloat);
        supplierMap.put(boolean.class, random::nextBoolean);
        supplierMap.put(Boolean.class, random::nextBoolean);
        supplierMap.put(Date.class, random::nextDate);
        supplierMap.put(BigInteger.class, random::nextBigInteger);
        supplierMap.put(BigDecimal.class, random::nextBigDecimal);
        supplierMap.put(Timestamp.class, random::nextTimestamp);
        supplierMap.put(Calendar.class, random::nextCalendar);
        supplierMap.put(GregorianCalendar.class, random::nextGregorianCalendar);
        supplierMap.put(XMLGregorianCalendar.class, random::nextXmlGregorianCalendar);

        return supplierMap;
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
