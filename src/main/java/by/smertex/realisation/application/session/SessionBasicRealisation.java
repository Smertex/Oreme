package by.smertex.realisation.application.session;

import by.smertex.annotation.entity.classes.Table;
import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.exceptions.application.SessionException;
import by.smertex.interfaces.application.builders.EntityCollector;
import by.smertex.interfaces.application.builders.QueryBuilder;
import by.smertex.interfaces.application.session.*;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.realisation.elements.IsolationLevel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

public class SessionBasicRealisation implements Session {

    private final Connection connection;
    private final QueryBuilder queryBuilder;
    private final EntityManager entityManager;
    private final ProxyEntityBuilder proxyEntityBuilder;
    private final Cache cache;
    private final Method methodForBuildEntityInstance;
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
        try {
            Object entityInCache = findEntityInCache(entity, compositeKey);
            if(entityInCache != null) return entityInCache;
        } catch (IllegalAccessException e) {
            throw new SessionException(e);
        }

        String sql = queryBuilder.selectSql(entity, compositeKey);
        System.out.println(sql);
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()){

            Object instance = null;
            if(resultSet.next()) instance = buildInstance(entity, resultSet);
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

    private Object findEntityInCache(Class<?> entity, CompositeKey compositeKey) throws IllegalAccessException {
        List<Object> entities = cache.getEntityInCacheByType(entity);
        List<Field> id = entityManager.isIdField(entity);
        Map<String, Object> compositeKeyMap = compositeKey.getKeys();
        boolean flag = false;

        for(Object instance: entities){
            for(Field field: id){
               flag = field.get(instance).equals(compositeKeyMap.get(field.getName()));
               if(!flag) break;
            }
            if(flag) return instance;
        }

        return null;
    }

    private Object buildInstance(Class<?> clazz, ResultSet fieldValues) {
        try {
            Object instanceClass = clazz.getDeclaredConstructor().newInstance();
            List<Field> fields = entityManager.getClassFields(instanceClass.getClass());

            for(Field field: fields){
                Object relationship = entityManager.isRelationship(field) ?
                        buildRelationshipEntityInstance(field, fieldValues) : fieldValues.getObject(rowGenerate(clazz, field));
                fieldSet(field, instanceClass, relationship);
            }
            cache.addEntityInCache(instanceClass);
            return instanceClass;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 SQLException e) {
            throw new SessionException(e);
        }
    }

    private Object buildRelationshipEntityInstance(Field field, ResultSet fieldValues) throws InstantiationException, IllegalAccessException, SQLException {
        CompositeKey key = new CompositeKeyBasicRealisation(field.getType());
        key.setValue("id", fieldValues.getObject(rowGenerate(field.getDeclaringClass(), field)));
        return entityManager.isLazyRelationship(field) ?
                proxyEntityBuilder.createProxy(field.getType(), methodForBuildEntityInstance, this, field.getType(), key)
                : buildInstance(field.getType(), fieldValues);
    }

    private void fieldSet(Field field, Object instance, Object relationship) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(instance, relationship);
    }

    private String rowGenerate(Class<?> entity, Field field){
        return entity.getAnnotation(Table.class).name() + EntityCollector.COLUMN_NAME_SEPARATOR + field.getAnnotation(Column.class).name();
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
                                      ProxyEntityBuilder proxyEntityBuilder,
                                      QueryBuilder queryBuilder){
        this.connection = connection;
        try {
            connection.setAutoCommit(false);
            this.methodForBuildEntityInstance = SessionBasicRealisation.class.getDeclaredMethod("find", Class.class, CompositeKey.class);
        } catch (SQLException | NoSuchMethodException e) {
            throw new SessionException(e);
        }
        setIsolationLevel(level);
        this.cache = cache;
        this.queryBuilder = queryBuilder;
        this.entityManager = entityManager;
        this.proxyEntityBuilder = proxyEntityBuilder;
    }
}
