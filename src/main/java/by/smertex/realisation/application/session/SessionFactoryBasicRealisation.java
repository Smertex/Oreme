package by.smertex.realisation.application.session;

import by.smertex.interfaces.application.builders.SessionQueryBuilder;
import by.smertex.interfaces.cfg.ConnectionManager;
import by.smertex.interfaces.application.session.Session;
import by.smertex.interfaces.application.session.SessionFactory;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.interfaces.cfg.ProxyEntityFactory;
import by.smertex.realisation.application.builders.SessionQueryBuilderBasicRealisation;
import by.smertex.realisation.elements.IsolationLevel;

public class SessionFactoryBasicRealisation implements SessionFactory {

    private final ConnectionManager connectionManager;

    private final IsolationLevel basicIsolationLevel;

    private final ProxyEntityFactory proxyEntityFactory;

    private final SessionQueryBuilder sessionQueryBuilder;


    @Override
    public Session openSession() {
        return new SessionBasicRealisation(
                connectionManager.getConnection(),
                basicIsolationLevel,
                proxyEntityFactory,
                sessionQueryBuilder);
    }

    public SessionFactoryBasicRealisation(ConnectionManager connectionManager, IsolationLevel basicIsolationLevel, ProxyEntityFactory proxyEntityManager, EntityManager entityManager) {
        this.connectionManager = connectionManager;
        this.proxyEntityFactory = proxyEntityManager;
        this.basicIsolationLevel = basicIsolationLevel;
        this.sessionQueryBuilder = new SessionQueryBuilderBasicRealisation(entityManager);
    }
}
