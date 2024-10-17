package by.smertex.interfaces.application.session;

import java.lang.reflect.Method;

public interface LazyInitializer {

    Object initialize(Object object);

    Session getSession();
}
