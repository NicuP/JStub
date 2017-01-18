package org.oss.jstub;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

/**
 * This class is used to stub fields that are of interface type. Because Java
 * reflection (additional library can do that) does not know to search for
 * classes that implement a given interface, interface fields are going to be
 * implemented with proxies of that interface, and the return type of each
 * method is going to be stubbed.
 *
 * @author nicu
 */
class InterfaceHandler implements InvocationHandler {
    private static StubFactory stubFactory = StubFactory.get();
    private static InvocationHandler invocationHandler = new InterfaceHandler();

    @SuppressWarnings("unchecked")
    <T> T createProxyStub(Class<T> cls) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class<?>[]{cls}, invocationHandler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        if ("void".equals(returnType.getName()) || "java.lang.Void".equals(returnType.getName())) {
            return null;
        }
        Type genericType = method.getGenericReturnType();
        Class<?>[] genericTypes = stubFactory.getGenericTypes(genericType);
        return stubFactory.createStub(returnType, genericTypes);
    }
}
