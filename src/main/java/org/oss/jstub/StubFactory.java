package org.oss.jstub;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Supplier;

/**
 * This class has the purpose of automatically creating stubs for POJOs that are
 * Java Beans, i.e. classes that have only public setters and getters on fields.
 * StubFactory automatically generates random values for these fields if they
 * are of the type:
 * <ul>
 * <li>Basic types: int, long, double, float, byte, char (including wrappers)</li>
 * <li>Java common used types: String, Date, BigInteger, BigDecimal</li>
 * <li>Any Enum</li>
 * <li>Interfaces that have return types any of the above - proxy is created</li>
 * <li>Array, List, Set or Map of type of any of the above</li>
 * <li>Any Java Bean that recursively decomposes to any of the above</li>
 * </ul>
 * <p>
 * There are two ways of instantiating, using the default and the custom
 * instance; using default instance creates random values for all the fields:
 * <pre class="code">
 * {@code
 * <p>
 * public class MyBean {
 * private String string;
 * private MyEnum enum;
 * private List<Integer> integers;
 * private Map<Double,Date> map;
 * private int[] ints;
 * //other fields, getters and setters
 * }
 * <p>
 * Bean myBean = StubFactory.instance().create(MyBean.class);
 * Map<String, BigInteger> map = stubFactory.create(Map.class, String.class, BigInteger.class);
 * }
 * </pre>
 * Besides the auto-generated random values, custom values can be provided for
 * certain bean values; talking for example the {@code MyBean} from above, if
 * custom values are needed for {@code string} field then the StubFactory needs
 * to be instantiated:
 * <pre class="code">
 * {@code
 * StubFactory stubFactory = StubFactory.customInstance()
 * .addCustomValue("setString", "ab", "c")
 * .addCustomValue("setOtherField", otherValues);
 * .build();
 * }
 * </pre>
 * In this way the value for {@code String string} field will be randomly
 * selected from those provided.
 */
public final class StubFactory {
    public static final int DEFAULT_COLLECTION_SIZE = 3;
    /*Used to generate proxies for interface references*/
    private static final InterfaceHandler interfaceHandler = new InterfaceHandler();
    /*used to stop infinite recursion*/
    private static final Map<String, Object> cache = new HashMap<>();
    private static StubFactory INSTANCE;

    private final DefaultValues values;
    private final int collectionSize;

    private StubFactory(int collectionSize) {
        this.values = new DefaultValues();
        this.collectionSize = collectionSize;
    }

    private StubFactory(DefaultValues defaultValues, int collectionSize) {
        this.values = defaultValues;
        this.collectionSize = collectionSize;
    }

    public static StubFactory instance() {
        return instance(DEFAULT_COLLECTION_SIZE);
    }

    public static synchronized StubFactory instance(int collectionSize) {
        if (INSTANCE == null) {
            INSTANCE = new StubFactory(collectionSize);
        }
        return INSTANCE;
    }

    public static CustomValuesBuilder customInstance(int collectionSize) {
        return new CustomValuesBuilder(collectionSize);
    }

    public static CustomValuesBuilder customInstance() {
        return new CustomValuesBuilder(DEFAULT_COLLECTION_SIZE);
    }

    public <C extends Collection<T>, T> C createStubCollection(int count,
                                                               Supplier<C> collectionSupplier,
                                                               Class<T> cls,
                                                               Class<?>... genericTypes) {
        cache.clear();
        C collection = collectionSupplier.get();
        for (int i = 0; i < count; i++) {
            T t = instance(cls, genericTypes);
            collection.add(t);
        }
        return collection;
    }

    public <T> T createStub(Class<T> cls, Class<?>... genericTypes) {
        cache.clear();
        return instance(cls, genericTypes);
    }

    @SuppressWarnings("unchecked")
    private <T> T instance(Class<T> cls, Class<?>... genericTypes) {
        if (values.isDefaultValueType(cls)) {
            return (T) values.randomDefaultValue(cls);
        }

        if (cls.isEnum()) {
            return generateEnum(cls);
        }

        if (cls.isArray()) {
            return generateArray(cls);
        }

        if (hasInterface(cls, Collection.class) || hasInterface(cls, Map.class)) {
            return (T) generateCollection(cls, (Class<T>[]) genericTypes);
        }

        if (cls.isInterface()) {
            return interfaceHandler.createProxyStub(cls);
        }

        T mockedObject = createInstance(cls);
        if (isNotSpecialValue(cls)) {
            cache.put(cls.getName(), mockedObject);
        }

        for (Method method : cls.getMethods()) {
            if (isValid(method)) {
                handle(mockedObject, method);
            } else if (hasOnlyGetter(method, cls)) {
                handleSpecial(mockedObject, method);
            }
        }
        return mockedObject;
    }

    @SuppressWarnings("unchecked")
    private <T> void handleSpecial(T mockedObject, Method method) {
        try {
            List list = (List) method.invoke(mockedObject);
            Type type = method.getGenericReturnType();
            Class<?>[] genericTypes = getGenericTypes(type);
            if (genericTypes.length == 0) {
                return;// wildcard type
            }
            Class<?> genericClass = genericTypes[0];
            if (list == null) {
                list = new ArrayList<>();
            }
            list.addAll(createStubCollection(collectionSize, ArrayList::new, genericClass));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot invoke method " + method
                    + " on object of type " + mockedObject.getClass().getName());
        }
    }

    boolean isNotSpecialValue(Class<?> cls) {
        return !(values.isDefaultValueType(cls) || Collection.class.isAssignableFrom(cls)
                || Map.class.isAssignableFrom(cls));
    }

    private <T> T generateEnum(Class<T> cls) {
        T[] enumConstants = cls.getEnumConstants();
        int randValue = values.getRandomGenerator().nextInt(enumConstants.length);
        return enumConstants[randValue];
    }

    @SuppressWarnings("unchecked")
    private <T> T generateArray(Class<T> cls) {
        Class<?> arrayType = cls.getComponentType();
        Object array = Array.newInstance(arrayType, collectionSize);
        for (int i = 0; i < collectionSize; i++) {
            Object value = instance(arrayType);
            Array.set(array, i, value);
        }
        return (T) array;
    }

    private <T> boolean hasInterface(Class<T> cls, Class<?> interf) {
        if (cls.getName().equals(interf.getName())) {
            return true;
        }
        for (Class<?> classInterface : cls.getInterfaces()) {
            if (interf.getName().equals(classInterface.getName())) {
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    // redundant final because annotation
    private final <T> Object generateCollection(Class<?> collectionType, Class<T>... genericTypes) {
        if (hasInterface(collectionType, Map.class)) {
            if (genericTypes.length != 2) {
                throw new IllegalArgumentException("A map needs two generic types.");
            }
            Map<T, T> map = new HashMap<>(collectionSize);
            for (int i = 0; i < collectionSize; i++) {
                T t = instance(genericTypes[0]);
                T v = instance(genericTypes[1]);
                map.put(t, v);
            }
            return map;
        } else {
            if (genericTypes.length != 1) {
                throw new IllegalArgumentException("A collection needs one generic type.");
            }
            Collection<T> collection;
            if (hasInterface(collectionType, Set.class)) {
                collection = new HashSet<>(collectionSize);
            } else {
                collection = new ArrayList<>(collectionSize);
            }
            for (int i = 0; i < collectionSize; i++) {
                T mock = instance(genericTypes[0]);
                collection.add(mock);
            }
            return collection;
        }
    }

    private <T> T createInstance(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException e) {
            return tryNonDefaultConstructors(cls);
        } catch (IllegalAccessException e) {
            return tryNonDefaultConstructors(cls);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T tryNonDefaultConstructors(Class<T> cls) {
        Constructor<?>[] constructors = cls.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] parameterObjects = new Object[parameterTypes.length];
            int i = 0;
            for (Class<?> parameterType : parameterTypes) {
                Type type = constructor.getGenericParameterTypes()[0];
                Class<?>[] genericTypes = getGenericTypes(type);
                parameterObjects[i++] = instance(parameterType, genericTypes);
            }
            try {
                return (T) constructor.newInstance(parameterObjects);
            } catch (InstantiationException | InvocationTargetException
                    | IllegalAccessException e) {
                // Let it try another constructor
            }
        }
        throw new IllegalArgumentException("Class " + cls.getName()
                + " does not have default constructor, "
                + " and did not found valid accessible constructor.");
    }

    private boolean isValid(Method method) {
        return method.getName().startsWith("set") && method.getParameterTypes().length == 1
                && !method.isSynthetic();
    }

    private boolean hasOnlyGetter(Method method, Class<?> cls) {
        String methodName = method.getName();
        String setterName = methodName.replaceFirst("g", "s");
        boolean isSpecialPartial = methodName.startsWith("get") &&
                method.getParameterTypes().length == 0 &&
                method.getReturnType().equals(List.class);
        if (isSpecialPartial) {
            for (Method innerMethod : cls.getMethods()) {
                if (innerMethod.getName().equals(setterName)) {
                    isSpecialPartial = false;
                }
            }
        }
        return isSpecialPartial;
    }

    private <T> void handle(T mockedObject, Method method) {
        Object value;
        String methodName = method.getName();
        if (values.isIgnoredSetter(methodName)) {
            return;
        }
        if (values.isCustomValue(methodName)) {
            value = values.getCustomValue(methodName);
        } else {
            Class<?> parameterClass = method.getParameterTypes()[0];
            String parameterClassName = parameterClass.getName();
            if (cache.containsKey(parameterClassName)) {
                value = cache.get(parameterClassName);
            } else if (hasInterface(parameterClass, Collection.class)
                    || hasInterface(parameterClass, Map.class)) {
                Type type = method.getGenericParameterTypes()[0];
                Class<?>[] genericTypes = getGenericTypes(type);
                value = instance(parameterClass, genericTypes);
            } else {
                value = instance(parameterClass);
            }
        }
        invoke(mockedObject, method, value);
    }

    Class<?>[] getGenericTypes(Type obj) {
        if (!(obj instanceof ParameterizedType)) {
            return new Class<?>[]{};
        }
        ParameterizedType parameterizedType = ParameterizedType.class.cast(obj);
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        Class<?>[] genericTypes = new Class<?>[typeArguments.length];
        for (int i = 0; i < typeArguments.length; i++) {
            if (!(typeArguments[i] instanceof Class)) {
                return new Class<?>[]{};
            }
            genericTypes[i] = Class.class.cast(typeArguments[i]);
        }
        return genericTypes;
    }

    private void invoke(Object object, Method method, Object value) {
        try {
            method.invoke(object, value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot invoke method '" + method.getName() +
                    "' of object of type '" + object.getClass() + "' with argument of type '"
                    + value.getClass() + "'", e);
        }

    }

    public static class CustomValuesBuilder {
        private final Map<String, Object[]> customValues;
        private final Map<String, Supplier<?>> customSuppliers;
        private final Set<String> ignoredSetters;
        private final int collectionSize;

        CustomValuesBuilder(int collectionSize) {
            this.customValues = new HashMap<>();
            this.customSuppliers = new HashMap<>();
            this.ignoredSetters = new HashSet<>();
            this.collectionSize = collectionSize;
        }

        public CustomValuesBuilder addIgnoredSetter(String setterName) {
            ignoredSetters.add(setterName);
            return this;
        }

        public CustomValuesBuilder addCustomSupplier(String setterName, Supplier<?> supplier) {
            if (setterName == null || setterName.isEmpty()) {
                throw new IllegalArgumentException("Emtpy setter name not allowerd");
            }
            customSuppliers.put(setterName, supplier);
            return this;
        }

        public CustomValuesBuilder addCustomValue(String setterName, Object... customValues) {
            if (setterName == null || setterName.isEmpty()) {
                throw new IllegalArgumentException("Emtpy setter name not allowerd");
            }
            if (customValues.length == 0) {
                throw new IllegalArgumentException("For setter name " + setterName
                        + " one or multiple custom values need to be provided.");
            }
            this.customValues.put(setterName, customValues);
            return this;
        }

        public <T> T createStub(Class<T> cls, Class<?>... genericTypes) {
            DefaultValues defaultValues =
                    new DefaultValues(customValues, customSuppliers, ignoredSetters);
            StubFactory stubFactory = new StubFactory(defaultValues, collectionSize);
            return stubFactory.createStub(cls, genericTypes);
        }

        public <C extends Collection<T>, T> C createStubCollection(int count,
                                                                   Supplier<C> collectionSupplier,
                                                                   Class<T> cls,
                                                                   Class<?>... genericTypes) {
            DefaultValues defaultValues =
                    new DefaultValues(customValues, customSuppliers, ignoredSetters);
            StubFactory stubFactory = new StubFactory(defaultValues, collectionSize);
            return stubFactory.createStubCollection(count,
                    collectionSupplier, cls, genericTypes);
        }

        public StubFactory build() {
            DefaultValues defaultValues =
                    new DefaultValues(customValues, customSuppliers, ignoredSetters);
            return new StubFactory(defaultValues, collectionSize);
        }
    }
}
