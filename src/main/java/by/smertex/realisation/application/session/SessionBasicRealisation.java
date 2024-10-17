package by.smertex.realisation.application.session;

import by.smertex.exceptions.application.SessionException;
import by.smertex.interfaces.application.builders.QueryBuilder;
import by.smertex.interfaces.application.session.*;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.interfaces.mapper.ResultSetToObjectMapper;
import by.smertex.realisation.elements.IsolationLevel;

import java.sql.*;
import java.util.*;

public class SessionBasicRealisation implements Session {

    private final Connection connection;
    private final QueryBuilder queryBuilder;
    private final EntityManager entityManager;
    private final Cache cache;
    private final LazyInitializer lazyInitializer;
    private final ResultSetToObjectMapper resultSetToObjectMapper;
    private Transaction transaction;

    public void beginTransaction() {
        this.transaction = new TransactionBasicRealisation(connection, this);
        this.transaction.begin();
    }

    public Transaction getTransaction() {
        return transaction;
    }

    @SuppressWarnings("all")
    public void setIsolationLevel(IsolationLevel level) {
        try {
            connection.setTransactionIsolation(level.getLevel());
        } catch (SQLException e) {
            throw new SessionException(e);
        }
    }

    public Object find(Class<?> entity, Long id) {
        CompositeKey key = new CompositeKeyBasicRealisation(entity);
        key.setValue("id", id);
        return find(entity, key);
    }

    public Object find(Class<?> entity, CompositeKey compositeKey) throws SessionException {
        Object instance = cache.getEntity(entity, compositeKey);
        if(instance != null) return instance;

        String sql = queryBuilder.selectSql(entity, compositeKey);
        System.out.println(sql);
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                instance = resultSetToObjectMapper.map(entity, resultSet);
                cache.addEntityInCache(entity, instance, compositeKey);
            }

            if(instance != null)
                objectToProxy(instance);

            return instance;
        } catch (SQLException e) {
            throw new SessionException(e);
        }
    }

    public List<Object> findAll(Class<?> entity) {
        return null;
    }

    public boolean update(Object entity) {
        return false;
    }

    public Object save(Object entity) {
        return null;
    }

    public boolean delete(Object entity) {
        return false;
    }

    private void objectToProxy(Object object){
        entityManager.getClassFields(object.getClass()).stream()
                .filter(entityManager::isRelationship)
                .forEach(field -> {
                    try {
                        Object o = field.get(object);
                        field.set(object, lazyInitializer.initialize(o));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public Boolean isClose() {
        try {
            return connection.isClosed();
        } catch (SQLException e) {
            throw new SessionException(e);
        }
    }

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
                                      EntityManager entityManager,
                                      ResultSetToObjectMapper resultSetToObjectMapper,
                                      LazyInitializerFactory lazyInitializerFactory,
                                      QueryBuilder queryBuilder){
        this.connection = connection;
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new SessionException(e);
        }
        setIsolationLevel(level);
        this.cache = cache;
        this.queryBuilder = queryBuilder;
        this.entityManager = entityManager;
        this.lazyInitializer = lazyInitializerFactory.createLazyInitializer(this);
        this.resultSetToObjectMapper = resultSetToObjectMapper;
    }
}
