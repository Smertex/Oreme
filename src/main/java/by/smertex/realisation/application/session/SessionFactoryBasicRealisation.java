package by.smertex.realisation.application.session;

import by.smertex.interfaces.application.builders.InstanceBuilder;
import by.smertex.interfaces.application.builders.QueryBuilder;
import by.smertex.interfaces.application.session.CacheFactory;
import by.smertex.interfaces.cfg.ConnectionManager;
import by.smertex.interfaces.application.session.Session;
import by.smertex.interfaces.application.session.SessionFactory;
import by.smertex.realisation.elements.IsolationLevel;

public class SessionFactoryBasicRealisation implements SessionFactory {

    private final ConnectionManager connectionManager;

    private final IsolationLevel basicIsolationLevel;

    private final InstanceBuilder instanceBuilder;

    private final QueryBuilder queryBuilder;

    private final CacheFactory cacheFactory;


    @Override
    public Session openSession() {
        return new SessionBasicRealisation(
                connectionManager.getConnection(),
                basicIsolationLevel,
                cacheFactory.createCache(),
                instanceBuilder,
                queryBuilder);
    }

    public SessionFactoryBasicRealisation(ConnectionManager connectionManager,
                                          IsolationLevel basicIsolationLevel,
                                          CacheFactory cacheFactory,
                                          InstanceBuilder instanceBuilder,
                                          QueryBuilder queryBuilder) {
        this.connectionManager = connectionManager;
        this.basicIsolationLevel = basicIsolationLevel;
        this.instanceBuilder = instanceBuilder;
        this.queryBuilder = queryBuilder;
        this.cacheFactory = cacheFactory;
    }
}
