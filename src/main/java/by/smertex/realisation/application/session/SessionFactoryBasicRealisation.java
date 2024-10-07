package by.smertex.realisation.application.session;

import by.smertex.interfaces.application.builders.InstanceBuilder;
import by.smertex.interfaces.application.builders.QueryBuilder;
import by.smertex.interfaces.application.session.ProxyEntityBuilder;
import by.smertex.interfaces.cfg.ConnectionManager;
import by.smertex.interfaces.application.session.Session;
import by.smertex.interfaces.application.session.SessionFactory;
import by.smertex.interfaces.mapper.Mapper;
import by.smertex.realisation.elements.IsolationLevel;

import java.sql.ResultSet;
import java.util.Map;

public class SessionFactoryBasicRealisation implements SessionFactory {

    private final ConnectionManager connectionManager;

    private final IsolationLevel basicIsolationLevel;

    private final Mapper<Map<String, Object>, ResultSet> mapper;

    private final InstanceBuilder instanceBuilder;

    private final QueryBuilder queryBuilder;

    private final ProxyEntityBuilder proxyEntityBuilder;

    @Override
    public Session openSession() {
        return new SessionBasicRealisation(
                connectionManager.getConnection(),
                basicIsolationLevel,
                mapper,
                instanceBuilder,
                queryBuilder,
                proxyEntityBuilder);
    }

    public SessionFactoryBasicRealisation(ConnectionManager connectionManager,
                                          IsolationLevel basicIsolationLevel,
                                          Mapper<Map<String, Object>, ResultSet> mapper,
                                          InstanceBuilder instanceBuilder,
                                          QueryBuilder queryBuilder,
                                          ProxyEntityBuilder proxyEntityBuilder) {
        this.connectionManager = connectionManager;
        this.basicIsolationLevel = basicIsolationLevel;
        this.mapper = mapper;
        this.instanceBuilder = instanceBuilder;
        this.queryBuilder = queryBuilder;
        this.proxyEntityBuilder = proxyEntityBuilder;
    }
}
