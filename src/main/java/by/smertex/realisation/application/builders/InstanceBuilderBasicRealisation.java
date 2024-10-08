package by.smertex.realisation.application.builders;

import by.smertex.annotation.entity.classes.Table;
import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.exceptions.application.InstanceBuilderException;
import by.smertex.interfaces.application.builders.InstanceBuilder;
import by.smertex.interfaces.application.session.ProxyEntityBuilder;
import by.smertex.interfaces.cfg.EntityManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InstanceBuilderBasicRealisation implements InstanceBuilder {

    private final EntityManager entityManager;

    private final ProxyEntityBuilder proxyEntityBuilder;

    @Override
    public Object buildInstance(Class<?> clazz, ResultSet fieldValues, Object proxyClass, Method method) {
        return substitutionValues(clazz, fieldValues);
    }

    private Object substitutionValues(Class<?> entity, ResultSet fieldValues){
        Object instance = instanceCreate(entity);
        List<Field> fields =  entityManager.getClassFields(instance.getClass());

        for(Field field: fields){
            Object relationshipEntity = getRelationshipEntity(entity, fieldValues, field);
            fieldSet(field, instance, relationshipEntity);
        }

        return instance;
    }

    private Object instanceCreate(Class<?> clazz){
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new InstanceBuilderException(e);
        }
    }

    private void fieldSet(Field field, Object instance, Object relationshipEntity){
        try {
            field.setAccessible(true);
            field.set(instance, relationshipEntity);
        } catch (IllegalAccessException e) {
            throw new InstanceBuilderException(e);
        }
    }

    private Object getRelationshipEntity(Class<?> entity, ResultSet fieldValues, Field field){
        try {
            return !isFieldIsJoin(entityManager.getRelationshipAnnotation(field)) ?
                    fieldValues.getObject(rowGenerate(entity, field)) : substitutionValues(field.getType(), fieldValues);
        } catch (SQLException e) {
            throw new InstanceBuilderException(e);
        }
    }

    private String rowGenerate(Class<?> entity, Field field){
        return entity.getAnnotation(Table.class).name() + COLUMN_NAME_SEPARATOR + field.getAnnotation(Column.class).name();
    }

    public InstanceBuilderBasicRealisation(EntityManager entityManager, ProxyEntityBuilder proxyEntityBuilder) {
        this.entityManager = entityManager;
        this.proxyEntityBuilder = proxyEntityBuilder;
    }
}
