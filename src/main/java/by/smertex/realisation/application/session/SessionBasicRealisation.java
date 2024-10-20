package by.smertex.realisation.application.session;

import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.exceptions.application.SessionException;
import by.smertex.interfaces.application.builders.QueryBuilder;
import by.smertex.interfaces.application.session.*;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.interfaces.mapper.ResultSetToObjectMapper;
import by.smertex.realisation.elements.IsolationLevel;

import java.lang.reflect.Field;
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

            objectInitialization(entity, instance);

            return instance;
        } catch (SQLException | IllegalAccessException e) {
            throw new SessionException(e);
        }
    }

    public List<Object> findAll(Class<?> entity) {
        List<Object> entities = new ArrayList<>();
        String sql = queryBuilder.selectSql(entity);

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Object instance = resultSetToObjectMapper.map(entity, resultSet);
                objectInitialization(entity, instance);
                entities.add(instance);
            }

        } catch (SQLException | IllegalAccessException e) {
            throw new SessionException(e);
        }

        return entities;
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

    private void objectInitialization(Class<?> entity, Object instance) throws IllegalAccessException {
        List<Field> fields = entityManager.getClassFields(entity).stream()
                .filter(entityManager::isRelationship)
                .toList();
        for (Field field : fields) {
            if (entityManager.isLazyRelationship(field)) {
                field.set(instance, lazyInitializer.initialize(field.get(instance)));
                break;
            }
            field.set(instance, find(field.getType(), listIdToCompositeKey(field.get(instance))));
        }
        cache.addEntityInCache(entity, instance, listIdToCompositeKey(instance));
    }

    private CompositeKey listIdToCompositeKey(Object object) throws IllegalAccessException {
        CompositeKey compositeKey = new CompositeKeyBasicRealisation(object.getClass());
        for(Field field: entityManager.isIdField(object.getClass()))
            compositeKey.setValue(field.getDeclaredAnnotation(Column.class).name(), field.get(object));
        return compositeKey;
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
            cache.clear();
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
        try {
            this.connection = connection;
            connection.setAutoCommit(false);
            setIsolationLevel(level);
            this.cache = cache;
            this.queryBuilder = queryBuilder;
            this.entityManager = entityManager;
            this.lazyInitializer = lazyInitializerFactory.createLazyInitializer(this);
            this.resultSetToObjectMapper = resultSetToObjectMapper;
        } catch (SQLException e) {
            throw new SessionException(e);
        }
    }
}
