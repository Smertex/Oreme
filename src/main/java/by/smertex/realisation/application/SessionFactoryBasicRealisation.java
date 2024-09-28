package by.smertex.realisation.application;

import by.smertex.interfaces.cfg.ConnectionManager;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.interfaces.cfg.InitializationManager;
import by.smertex.interfaces.application.Session;
import by.smertex.interfaces.application.SessionFactory;
import by.smertex.realisation.elements.IsolationLevel;

public class SessionFactoryBasicRealisation implements SessionFactory {

    private final ConnectionManager connectionManager;

    private final EntityManager entityManager;

    private final IsolationLevel basicIsolationLevel;

    @Override
    public Session openSession() {
        return new SessionBasicRealisation(entityManager,
                connectionManager.getConnection(),
                basicIsolationLevel);
    }

    protected SessionFactoryBasicRealisation(ConnectionManager connectionManager, EntityManager entityManager, IsolationLevel basicIsolationLevel) {
        this.connectionManager = connectionManager;
        this.entityManager = entityManager;
        this.basicIsolationLevel = basicIsolationLevel;
    }
}
