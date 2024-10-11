package by.smertex.realisation.application.session;

import by.smertex.exceptions.application.SessionException;
import by.smertex.interfaces.application.builders.InstanceBuilder;
import by.smertex.interfaces.application.builders.QueryBuilder;
import by.smertex.interfaces.application.session.*;
import by.smertex.realisation.elements.IsolationLevel;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

public class SessionBasicRealisation implements Session {

    private final Connection connection;

    private final InstanceBuilder instanceBuilder;

    private final QueryBuilder queryBuilder;

    private final Cache cache;

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
    public Optional<Object> find(Class<?> entity, CompositeKey compositeKey) throws SessionException {
        String sql = queryBuilder.selectSql(entity, compositeKey);

        try(PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()){
            Method method = getClass().getMethod("find", Class.class, CompositeKey.class);

            Object instanceEntity = null;
            if(resultSet.next()) {
                //instanceEntity = instanceBuilder.buildInstance(entity, resultSet);
                instanceEntity = instanceBuilder.buildInstance(resultSet, entity, method, this);

                cache.addEntityInCache(instanceEntity);
            }
            return Optional.ofNullable(instanceEntity);
        } catch (SQLException | NoSuchMethodException e) {
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
                                      Cache cache,
                                      InstanceBuilder instanceBuilder,
                                      QueryBuilder queryBuilder){
        this.connection = connection;
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new SessionException(e);
        }
        setIsolationLevel(level);
        this.cache = cache;
        this.instanceBuilder = instanceBuilder;
        this.queryBuilder = queryBuilder;
    }
}
