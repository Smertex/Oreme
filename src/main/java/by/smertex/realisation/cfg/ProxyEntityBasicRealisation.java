package by.smertex.realisation.cfg;

import by.smertex.exceptions.cfg.ProxyEntityException;
import by.smertex.interfaces.application.builders.InstanceBuilder;
import by.smertex.interfaces.application.builders.ProxyEntityQueryBuilder;
import by.smertex.interfaces.application.session.CompositeKey;
import by.smertex.interfaces.cfg.ProxyEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProxyEntityBasicRealisation implements ProxyEntity {

    private Object entity;

    private final Class<?> entityClass;

    private final Object id;

    private final Connection connection;

    private final ProxyEntityQueryBuilder proxyEntityQueryBuilder;

    private final InstanceBuilder instanceBuilder;

    protected ProxyEntityBasicRealisation(Class<?> entityClass, Object id, Connection connection, ProxyEntityQueryBuilder proxyEntityQueryBuilder, InstanceBuilder instanceBuilder) {
        this.entityClass = entityClass;
        this.id = id;
        this.proxyEntityQueryBuilder = proxyEntityQueryBuilder;
        this.instanceBuilder = instanceBuilder;
        this.connection = connection;
    }

    @Override
    public Object getEntity() {
        if(entity == null) createEntity();
        close();
        return entity;
    }

    private void createEntity(){
        String sql = id instanceof CompositeKey ?
                proxyEntityQueryBuilder.selectWhereSql(entityClass, (CompositeKey) id)
                :  proxyEntityQueryBuilder.selectWhereSql(entityClass, (Long) id);

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            if(resultSet.next()) entity = instanceBuilder.buildInstance(entityClass, resultSet);

        } catch (SQLException e) {
            throw new ProxyEntityException(e);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new ProxyEntityException(e);
        }
    }
}
