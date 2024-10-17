package by.smertex.realisation.application.session;

import by.smertex.interfaces.application.session.LazyInitializer;
import by.smertex.interfaces.application.session.LazyInitializerFactory;
import by.smertex.interfaces.application.session.ProxyEntityBuilder;
import by.smertex.interfaces.application.session.Session;
import by.smertex.interfaces.cfg.EntityManager;

public class LazyInitializerFactoryBasicRealisation implements LazyInitializerFactory {
    private final ProxyEntityBuilder proxyEntityBuilder;
    private final EntityManager entityManager;

    public LazyInitializerFactoryBasicRealisation(ProxyEntityBuilder proxyEntityBuilder, EntityManager entityManager){
        this.proxyEntityBuilder = proxyEntityBuilder;
        this.entityManager = entityManager;
    }

    @Override
    public LazyInitializer createLazyInitializer(Session session) {
        return new LazyInitializerBasicRealisation(session, proxyEntityBuilder, entityManager);
    }
}
