package by.smertex.realisation.application;

import by.smertex.annotation.entity.classes.Table;
import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.exceptions.application.InstanceBuilderException;
import by.smertex.interfaces.application.InstanceBuilder;
import by.smertex.interfaces.cfg.EntityManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InstanceBuilderBasicRealisation implements InstanceBuilder {

    private final EntityManager entityManager;

    @Override
    public Object buildInstance(Class<?> clazz, ResultSet fieldValues) {
        return substitutionValues(clazz, fieldValues);
    }

    private Object substitutionValues(Class<?> entity, ResultSet fieldValues){
        Object object = instanceCreate(entity);
        List<Field> fields =  entityManager.getClassFields(object.getClass());

        for(Field field: fields){
            try {
                field.setAccessible(true);
                String row = entity.getAnnotation(Table.class).name() + "_" + field.getAnnotation(Column.class).name();
                Object relationshipEntity = !fieldHaveAnnotationRelationship(field) ?
                        fieldValues.getObject(row) : substitutionValues(field.getType(), fieldValues);
                field.set(object, relationshipEntity);
            } catch (IllegalAccessException | SQLException e) {
                throw new InstanceBuilderException(e);
            }
        }

        return object;
    }

    private Object instanceCreate(Class<?> clazz){
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new InstanceBuilderException(e);
        }
    }

    protected InstanceBuilderBasicRealisation(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
