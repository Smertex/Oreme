package by.smertex.realisation.application.builders;

import by.smertex.exceptions.application.ProxyInstanceException;
import by.smertex.interfaces.application.session.ProxyEntityBuilder;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProxyEntityBuilderBasicRealisation implements ProxyEntityBuilder {
    private final ProxyFactory proxyFactory = new ProxyFactory();

    public Object createProxy(Class<?> clazz, Method method, Object objectInWhichMethod, Object... args) throws InstantiationException, IllegalAccessException {
        MethodHandler handler = createHandler(method, objectInWhichMethod, args);
        return createProxyInstance(clazz, handler);
    }

    public Object unproxy(Object proxy) {
        if (proxy instanceof ProxyObject) {
            MethodHandler handler = ((ProxyObject) proxy).getHandler();

            if (handler != null) {
                try {
                    return handler.getClass().getMethod("getOriginalObject").invoke(handler);
                } catch (Exception e) {
                    throw new ProxyInstanceException(e);
                }
            }
        }
        return proxy;
    }

    @SuppressWarnings("deprecation")
    private Object createProxyInstance(Class<?> clazz, MethodHandler handler) throws InstantiationException, IllegalAccessException {
        proxyFactory.setSuperclass(clazz);
        Object instance = proxyFactory.createClass().newInstance();
        ((ProxyObject) instance).setHandler(handler);
        return instance;
    }

    protected MethodHandler createHandler(Method methodByCreate, Object objectInWhichMethod, Object... argsMethodByCreate){
        Object[] object = new Object[]{null};
        return new MethodHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Method proceed, Object[] args) throws Throwable {
                if (object[0] == null)
                    object[0] = methodByCreate.invoke(objectInWhichMethod, argsMethodByCreate);

                return method.invoke(object[0], args);
            }

            public Object getOriginalObject() {
                if (object[0] == null) {
                    try {
                        object[0] = methodByCreate.invoke(objectInWhichMethod, argsMethodByCreate);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }

                return object[0];
            }
        };

//        return (proxy, method, proceed, args) -> {
//            try {
//                if (object[0] == null) object[0] = methodByCreate.invoke(objectInWhichMethod, argsMethodByCreate);
//                return method.invoke(object[0], args);
//            } catch (Exception e){
//                throw new ProxyInstanceException(e);
//            }
//        };
    }
}
