package by.smertex.realisation.application;

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
        Object object = instanceCreate(clazz);
        substitutionValues(object, fieldValues);
        return object;
    }

    private Object instanceCreate(Class<?> clazz){
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new InstanceBuilderException(e);
        }
    }

    private void substitutionValues(Object entity, ResultSet fieldValues){
        List<Field> fields =  entityManager.getClassFields(entity.getClass());

        for(Field field: fields){
            try {
                field.setAccessible(true);
                field.set(entity, fieldValues.getObject(field.getAnnotation(Column.class).name()));
            } catch (IllegalAccessException | SQLException e) {
                throw new InstanceBuilderException(e);
            }
        }
    }

    protected InstanceBuilderBasicRealisation(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
