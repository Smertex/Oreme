package by.smertex.realisation.application.session;

import by.smertex.interfaces.application.builders.InstanceBuilder;
import by.smertex.interfaces.application.builders.ProxyEntityQueryBuilder;
import by.smertex.interfaces.application.session.ProxyEntity;
import by.smertex.interfaces.application.session.ProxyEntityFactory;

import java.sql.Connection;

public class ProxyEntityFactoryBasicRealisation implements ProxyEntityFactory {

    private final Connection connection;

    private final ProxyEntityQueryBuilder proxyEntityQueryBuilder;

    private final InstanceBuilder instanceBuilder;

    @Override
    public ProxyEntity buildProxyEntity(Class<?> entity, Object id) {
        return new ProxyEntityBasicRealisation(entity, id,
                connection,
                proxyEntityQueryBuilder,
                instanceBuilder);
    }

    public ProxyEntityFactoryBasicRealisation(Connection connection,
                                              ProxyEntityQueryBuilder proxyEntityQueryBuilder,
                                              InstanceBuilder instanceBuilder) {
        this.connection = connection;
        this.proxyEntityQueryBuilder = proxyEntityQueryBuilder;
        this.instanceBuilder = instanceBuilder;
    }
}
