package by.smertex.realisation.application.session;

import by.smertex.interfaces.application.session.InstanceBuilder;
import by.smertex.interfaces.application.session.QueryBuilder;
import by.smertex.interfaces.cfg.ConnectionManager;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.interfaces.application.session.Session;
import by.smertex.interfaces.application.session.SessionFactory;
import by.smertex.realisation.elements.IsolationLevel;

public class SessionFactoryBasicRealisation implements SessionFactory {

    private final ConnectionManager connectionManager;

    private final IsolationLevel basicIsolationLevel;

    private final QueryBuilder queryBuilder;

    private final InstanceBuilder instanceBuilder;

    @Override
    public Session openSession() {
        return new SessionBasicRealisation(
                queryBuilder,
                connectionManager.getConnection(),
                basicIsolationLevel,
                instanceBuilder);
    }

    public SessionFactoryBasicRealisation(ConnectionManager connectionManager, EntityManager entityManager, IsolationLevel basicIsolationLevel) {
        this.connectionManager = connectionManager;
        this.basicIsolationLevel = basicIsolationLevel;
        this.queryBuilder = new QueryBuilderBasicRealisation(entityManager);
        this.instanceBuilder = new InstanceBuilderBasicRealisation(entityManager);
    }
}
