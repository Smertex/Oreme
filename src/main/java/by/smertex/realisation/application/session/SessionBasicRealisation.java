package by.smertex.realisation.application.session;

import by.smertex.exceptions.application.SessionException;
import by.smertex.interfaces.application.session.*;
import by.smertex.realisation.elements.IsolationLevel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionBasicRealisation implements Session {
    private final Connection connection;

    private final QueryBuilder queryBuilder;

    private final InstanceBuilder instanceBuilder;

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
        String sql = queryBuilder.selectWhereSql(entity, id);

        try(PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {
            if(resultSet.next()) return Optional.of(instanceBuilder.buildInstance(entity, resultSet));
            return Optional.empty();
        } catch (SQLException e) {
            throw new SessionException(e);
        }
    }

    @Override
    public Optional<Object> find(Class<?> entity, CompositeKey compositeKey) {
        String sql = queryBuilder.selectWhereSql(entity, compositeKey);

        try(PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {
            if(resultSet.next()) return Optional.of(instanceBuilder.buildInstance(entity, resultSet));
            return Optional.empty();
        } catch (SQLException e) {
            throw new SessionException(e);
        }
    }

    @Override
    public List<Object> findAll(Class<?> entity) {
        String sql = queryBuilder.selectFieldsSql(entity);
        List<Object> entities = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next())
                entities.add(instanceBuilder.buildInstance(entity, resultSet));
        } catch (SQLException e) {
            throw new SessionException(e);
        }
        return entities;
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

    protected SessionBasicRealisation(QueryBuilder queryBuilder, Connection connection, IsolationLevel level, InstanceBuilder instanceBuilder){
        this.connection = connection;
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new SessionException(e);
        }
        setIsolationLevel(level);
        this.queryBuilder = queryBuilder;
        this.instanceBuilder = instanceBuilder;
    }
}