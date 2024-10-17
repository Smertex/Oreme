package by.smertex.realisation.application.session;

import by.smertex.interfaces.application.builders.QueryBuilder;
import by.smertex.interfaces.application.session.CacheFactory;
import by.smertex.interfaces.application.session.ProxyEntityBuilder;
import by.smertex.interfaces.cfg.ConnectionManager;
import by.smertex.interfaces.application.session.Session;
import by.smertex.interfaces.application.session.SessionFactory;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.realisation.elements.IsolationLevel;

public class SessionFactoryBasicRealisation implements SessionFactory {

    private final ConnectionManager connectionManager;

    private final IsolationLevel basicIsolationLevel;

    private final QueryBuilder queryBuilder;

    private final EntityManager entityManager;

    private final CacheFactory cacheFactory;

    private final ProxyEntityBuilder proxyEntityBuilder;

    @Override
    public Session openSession() {
        return new SessionBasicRealisation(
                connectionManager.getConnection(),
                basicIsolationLevel,
                cacheFactory.createCache(),
                entityManager,
                proxyEntityBuilder,
                queryBuilder);
    }

    public SessionFactoryBasicRealisation(ConnectionManager connectionManager,
                                          IsolationLevel basicIsolationLevel,
                                          CacheFactory cacheFactory,
                                          EntityManager entityManager,
                                          ProxyEntityBuilder proxyEntityBuilder,
                                          QueryBuilder queryBuilder) {
        this.connectionManager = connectionManager;
        this.basicIsolationLevel = basicIsolationLevel;
        this.queryBuilder = queryBuilder;
        this.cacheFactory = cacheFactory;
        this.entityManager = entityManager;
        this.proxyEntityBuilder = proxyEntityBuilder;
    }
}
