package by.smertex.realisation.cfg;

import by.smertex.interfaces.application.builders.InstanceBuilder;
import by.smertex.interfaces.application.builders.ProxyEntityQueryBuilder;
import by.smertex.interfaces.cfg.ConnectionManager;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.interfaces.cfg.ProxyEntity;
import by.smertex.interfaces.cfg.ProxyEntityFactory;
import by.smertex.realisation.application.builders.InstanceBuilderBasicRealisation;
import by.smertex.realisation.application.builders.ProxyEntityQueryBuilderBasicRealisation;

public class ProxyEntityFactoryBasicRealisation implements ProxyEntityFactory {

    private final ConnectionManager connectionManager;

    private final ProxyEntityQueryBuilder proxyEntityQueryBuilder;

    private final InstanceBuilder instanceBuilder;

    @Override
    public ProxyEntity buildProxyEntity(Class<?> entity, Object id) {
        return new ProxyEntityBasicRealisation(entity, id,
                connectionManager.getConnection(),
                proxyEntityQueryBuilder,
                instanceBuilder);
    }

    public ProxyEntityFactoryBasicRealisation(ConnectionManager connectionManager, EntityManager entityManager) {
        this.connectionManager = connectionManager;
        this.proxyEntityQueryBuilder = new ProxyEntityQueryBuilderBasicRealisation(entityManager);
        this.instanceBuilder = new InstanceBuilderBasicRealisation(entityManager);
    }
}
