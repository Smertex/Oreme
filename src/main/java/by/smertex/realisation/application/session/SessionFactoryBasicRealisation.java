package by.smertex.realisation.application.session;

import by.smertex.interfaces.application.builders.QueryBuilder;
import by.smertex.interfaces.application.session.*;
import by.smertex.interfaces.cfg.ConnectionManager;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.realisation.elements.IsolationLevel;

public class SessionFactoryBasicRealisation implements SessionFactory {

    private final ConnectionManager connectionManager;

    private final IsolationLevel basicIsolationLevel;

    private final QueryBuilder queryBuilder;

    private final EntityManager entityManager;

    private final CacheFactory cacheFactory;

    private final LazyInitializerFactory lazyInitializerFactory;

    @Override
    public Session openSession() {
        return new SessionBasicRealisation(
                connectionManager.getConnection(),
                basicIsolationLevel,
                cacheFactory.createCache(),
                entityManager,
                lazyInitializerFactory,
                queryBuilder);
    }

    public SessionFactoryBasicRealisation(ConnectionManager connectionManager,
                                          IsolationLevel basicIsolationLevel,
                                          CacheFactory cacheFactory,
                                          EntityManager entityManager,
                                          LazyInitializerFactory lazyInitializerFactory,
                                          QueryBuilder queryBuilder) {
        this.connectionManager = connectionManager;
        this.basicIsolationLevel = basicIsolationLevel;
        this.queryBuilder = queryBuilder;
        this.cacheFactory = cacheFactory;
        this.entityManager = entityManager;
        this.lazyInitializerFactory = lazyInitializerFactory;
    }
}
