package by.smertex.realisation.application.builders;

import by.smertex.interfaces.application.session.ProxyEntityBuilder;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import java.lang.reflect.Method;

public class ProxyEntityBuilderBasicRealisation implements ProxyEntityBuilder {
    private final ProxyFactory proxyFactory = new ProxyFactory();

    public Object createProxy(Class<?> clazz, Method method, Object objectInWhichMethod, Object... args) throws InstantiationException, IllegalAccessException {
        MethodHandler handler = createHandler(method, objectInWhichMethod, args);
        return createProxyInstance(clazz, handler);
    }

    @SuppressWarnings("deprecation")
    private Object createProxyInstance(Class<?> clazz, MethodHandler handler) throws InstantiationException, IllegalAccessException {
        proxyFactory.setSuperclass(clazz);
        Object instance = proxyFactory.createClass().newInstance();
        ((ProxyObject) instance).setHandler(handler);
        return instance;
    }

    private MethodHandler createHandler(Method methodByCreate, Object objectInWhichMethod, Object... argsMethodByCreate){
        Object[] object = new Object[]{null};

        return (proxy, method, proceed, args) -> {
            if(object[0] == null) object[0] = methodByCreate.invoke(objectInWhichMethod, argsMethodByCreate);
            return method.invoke(object[0], args);
        };
    }
}
