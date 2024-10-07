package by.smertex.realisation.application.session;

import by.smertex.exceptions.application.SessionException;
import by.smertex.interfaces.application.builders.InstanceBuilder;
import by.smertex.interfaces.application.builders.QueryBuilder;
import by.smertex.interfaces.application.session.*;
import by.smertex.interfaces.mapper.Mapper;
import by.smertex.realisation.elements.IsolationLevel;

import java.sql.*;
import java.util.*;

public class SessionBasicRealisation implements Session {

    private final Mapper<Map<String, Object>, ResultSet> mapper;

    private final Connection connection;

    private final InstanceBuilder instanceBuilder;

    private final QueryBuilder queryBuilder;

    private final ProxyEntityBuilder proxyEntityBuilder;

    private Transaction transaction;

    @Override
    public void beginTransaction() {
        this.transaction = new TransactionBasicRealisation(connection, this);
        this.transaction.begin();
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public void setIsolationLevel(IsolationLevel level) {
        try {
            connection.setTransactionIsolation(level.getLevel());
        } catch (SQLException e) {
            throw new SessionException(e);
        }
    }

    @Override
    public Optional<Object> find(Class<?> entity, Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> find(Class<?> entity, CompositeKey compositeKey) {
        String sql = queryBuilder.selectSql(entity, compositeKey);

        try(PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()){
            Object instanceEntity = null;
            if(resultSet.next()) instanceEntity = instanceBuilder.buildInstance(entity, resultSet);
            if(instanceEntity != null) transaction.addEntityInTransaction(instanceEntity);
            return Optional.ofNullable(instanceEntity);
        } catch (SQLException e) {
            throw new SessionException(e);
        }
    }

    @Override
    public List<Object> findAll(Class<?> entity) {
        return null;
    }

    @Override
    public boolean update(Object entity) {
        return false;
    }

    @Override
    public Object save(Object entity) {
        return null;
    }

    @Override
    public boolean delete(Object entity) {
        return false;
    }

    @Override
    public void close() {
        if(transaction != null) transaction.commit();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new SessionException(e);
        }
    }


    protected SessionBasicRealisation(Connection connection,
                                      IsolationLevel level,
                                      Mapper<Map<String, Object>, ResultSet> mapper,
                                      InstanceBuilder instanceBuilder,
                                      QueryBuilder queryBuilder,
                                      ProxyEntityBuilder proxyEntityBuilder){
        this.connection = connection;
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new SessionException(e);
        }
        setIsolationLevel(level);
        this.mapper = mapper;
        this.instanceBuilder = instanceBuilder;
        this.queryBuilder = queryBuilder;
        this.proxyEntityBuilder = proxyEntityBuilder;
    }
}
