package by.smertex.realisation.application.session;

import by.smertex.interfaces.application.builders.SessionQueryBuilder;
import by.smertex.interfaces.cfg.ConnectionManager;
import by.smertex.interfaces.application.session.Session;
import by.smertex.interfaces.application.session.SessionFactory;
import by.smertex.interfaces.application.session.ProxyEntityFactory;
import by.smertex.interfaces.mapper.Mapper;
import by.smertex.realisation.elements.IsolationLevel;

import java.sql.ResultSet;
import java.util.Map;

public class SessionFactoryBasicRealisation implements SessionFactory {

    private final ConnectionManager connectionManager;

    private final IsolationLevel basicIsolationLevel;

    private final ProxyEntityFactory proxyEntityFactory;

    private final SessionQueryBuilder sessionQueryBuilder;

    private final Mapper<Map<String, Object>, ResultSet> mapper;


    @Override
    public Session openSession() {
        return new SessionBasicRealisation(
                connectionManager.getConnection(),
                basicIsolationLevel,
                proxyEntityFactory,
                sessionQueryBuilder,
                mapper);
    }

    public SessionFactoryBasicRealisation(ConnectionManager connectionManager,
                                          IsolationLevel basicIsolationLevel,
                                          ProxyEntityFactory proxyEntityManager,
                                          Mapper<Map<String, Object>, ResultSet> mapper,
                                          SessionQueryBuilder sessionQueryBuilder) {
        this.connectionManager = connectionManager;
        this.proxyEntityFactory = proxyEntityManager;
        this.basicIsolationLevel = basicIsolationLevel;
        this.sessionQueryBuilder = sessionQueryBuilder;
        this.mapper = mapper;
    }
}
