package by.smertex.realisation.application.session;

import by.smertex.annotation.entity.classes.Entity;
import by.smertex.exceptions.application.SessionException;
import by.smertex.interfaces.application.builders.QueryBuilder;
import by.smertex.interfaces.application.session.*;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.interfaces.mapper.ResultSetToObjectMapper;
import by.smertex.realisation.elements.IsolationLevel;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

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
        CompositeKey compositeKey = new CompositeKeyBasicRealisation(entity);
        compositeKey.setValue("id", id);
        return find(entity, compositeKey);
    }

    public Object find(Class<?> entity, CompositeKey compositeKey) {
        Object entityInstance = cache.getEntity(entity, compositeKey);
        if(entityInstance != null)
            return entityInstance;

        String sql = queryBuilder.selectSql(entity, compositeKey);
        List<Object> entityInstanceList = findObjectByQuery(entity, sql);

        if(entityInstanceList.size() > 1)
            throw new SessionException(new RuntimeException());
        if(entityInstanceList.isEmpty())
            return null;

        entityInstance = entityInstanceList.get(0);
        //initFieldRelationshipToMany(entity, entityInstance);

        return entityInstance;
    }

    public List<Object> findAll(Class<?> entity) {
        String sql = queryBuilder.selectSql(entity);
        return findObjectByQuery(entity, sql);
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

    private List<Object> findObjectByQuery(Class<?> entity, String sql) {
        List<Object> entityInstanceList = new ArrayList<>();

        System.out.println(sql);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Object entityInstance = findEntityInstanceInCache(resultSetToObjectMapper.map(entity, resultSet));
                //initRelationshipInEntityInstance(entityInstance);
                entityInstanceList.add(entityInstance);
            }

        } catch (SQLException e) {
            throw new SessionException(e);
        }

        return entityInstanceList;
    }

    private Object findEntityInstanceInCache(Object entityInstance) {
        Class<?> entity = entityInstance.getClass();
        CompositeKey entityInstanceCompositeKey = Session.listIdToCompositeKey(entityManager, entityInstance);
        Object entityInstanceInCache = cache.getEntity(entity, entityInstanceCompositeKey);

        if(entityInstanceInCache == null){
            cache.addEntityInCache(entity, entityInstance, entityInstanceCompositeKey);
            entityInstanceInCache = entityInstance;
            initRelationshipInEntityInstance(entityInstance);
        }

        return entityInstanceInCache;
    }

    private void initRelationshipInEntityInstance(Object entityInstance){
        Class<?> entity = entityInstance.getClass();
        entityManager.getClassFields(entity).stream()
                .filter(entityManager::isRelationship)
                .forEach(field -> initFieldRelationshipToOne(field, entityInstance));
        entityManager.getToManyRelationship(entity)
                .forEach(field -> initFieldRelationshipToMany(field, entityInstance));
    }

    private void initFieldRelationshipToOne(Field field, Object entityInstance){
        try {
            Object relationshipEntityInstance = field.get(entityInstance);

            if(entityManager.isLazyRelationship(field)) {
                Object proxy = lazyInitializer.initialize(relationshipEntityInstance);
                field.set(entityInstance, proxy);
                return;
            }

            CompositeKey compositeKey = Session.listIdToCompositeKey(entityManager, relationshipEntityInstance);
            field.set(entityInstance, find(field.getType(), compositeKey));

        } catch (IllegalAccessException e) {
            throw new SessionException(e);
        }
    }

    private void initFieldRelationshipToMany(Field relationshipToManyField, Object entityInstance){
        try {
            relationshipToManyField.setAccessible(true);
            List<Object> relationshipEntityList = (List<Object>) relationshipToManyField.get(entityInstance);

            String relationshipMappedByColumn = entityManager.getRelationshipMappedBy(relationshipToManyField);
            Object idRelationship = entityManager.getFieldIdByKeyFor(entityInstance.getClass(), relationshipMappedByColumn).get(entityInstance);
            Class<?> classRelationship = entityManager.getGenericTypeInRelationshipCollection(relationshipToManyField);

            CompositeKey compositeKey = new CompositeKeyBasicRealisation(Map.of(relationshipMappedByColumn, idRelationship));
            String sql = queryBuilder.selectSql(classRelationship, compositeKey);
            relationshipEntityList.addAll(findObjectByQuery(classRelationship, sql));

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

//        List<Field> relationshipToMany = entityManager.getToManyRelationship(entity);
//
//        for(Field field: relationshipToMany) {
//            field.setAccessible(true);
//            try {
//                List<Object> relationshipInEntity = (List<Object>) field.get(entityInstance);
//
//                String relationshipMappedByColumn = entityManager.getRelationshipMappedBy(field);
//                Object idRelationship = entityManager.getFieldIdByKeyFor(entity, relationshipMappedByColumn).get(entityInstance);
//                Class<?> classRelationship = entityManager.getGenericTypeInRelationshipCollection(field);
//
//                CompositeKey compositeKey = new CompositeKeyBasicRealisation(Map.of(relationshipMappedByColumn, idRelationship));
//                String sql = queryBuilder.selectSql(classRelationship, compositeKey);
//
//                relationshipInEntity.addAll(findObjectByQuery(classRelationship, sql));
//
//
//            } catch (IllegalAccessException e) {
//                throw new SessionException(e);
//            }
//        }
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
