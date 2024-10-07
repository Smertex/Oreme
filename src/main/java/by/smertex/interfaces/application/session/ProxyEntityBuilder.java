package by.smertex.interfaces.application.session;

import java.lang.reflect.Method;

public interface ProxyEntityBuilder {

    Object createProxy(Class<?> clazz, Method method, Object methodInstance, Object... args) throws InstantiationException, IllegalAccessException;

}
