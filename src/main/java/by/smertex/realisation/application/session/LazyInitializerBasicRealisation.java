package by.smertex.realisation.application.session;

import by.smertex.exceptions.application.LazyInitializationException;
import by.smertex.interfaces.application.session.CompositeKey;
import by.smertex.interfaces.application.session.LazyInitializer;
import by.smertex.interfaces.application.session.ProxyEntityBuilder;
import by.smertex.interfaces.application.session.Session;
import by.smertex.interfaces.cfg.EntityManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class LazyInitializerBasicRealisation implements LazyInitializer {

    private final Session session;
    private final ProxyEntityBuilder proxyEntityBuilder;
    private final EntityManager entityManager;
    private Method method;

    public LazyInitializerBasicRealisation(Session session, ProxyEntityBuilder proxyEntityBuilder, EntityManager entityManager){
        this.session = session;
        this.proxyEntityBuilder = proxyEntityBuilder;
        this.entityManager = entityManager;
        initializationInitMethod();
    }

    @Override
    public Object initialize(Object object) {
        if (session.isClose()) throw new LazyInitializationException(new RuntimeException());

        Class<?> objectClass = object.getClass();
        List<Field> idFields = entityManager.getIdField(objectClass);
        CompositeKey compositeKey = new CompositeKeyBasicRealisation(objectClass);

        try {
            for (Field field : idFields)
                compositeKey.setValue(field.getName(), field.get(object));
            return proxyEntityBuilder.createProxy(objectClass, method, session, objectClass, compositeKey);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new LazyInitializationException(e);
        }

    }

    @Override
    public Session getSession() {
        return session;
    }

    private void initializationInitMethod(){
        try {
            this.method = Session.class.getDeclaredMethod("find", Class.class, CompositeKey.class);
        } catch (NoSuchMethodException e) {
            throw new LazyInitializationException(e);
        }
    }
}
