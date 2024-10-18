package by.smertex.realisation.mappers;

import by.smertex.annotation.entity.classes.Table;
import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.annotation.entity.fields.columns.EnumColumn;
import by.smertex.annotation.entity.fields.columns.EnumValueType;
import by.smertex.exceptions.mapper.ResultSetToObjectMapperException;
import by.smertex.interfaces.application.builders.EntityCollector;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.interfaces.mapper.ResultSetToObjectMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ResultSetToObjectMapperBasicRealisation implements ResultSetToObjectMapper {

    private final EntityManager entityManager;

    public ResultSetToObjectMapperBasicRealisation(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Object map(Class<?> objectClass, ResultSet resultSet) {
        try {
            Object instanceClass = objectClass.getDeclaredConstructor().newInstance();
            List<Field> fields = entityManager.getClassFields(instanceClass.getClass());

            for(Field field: fields) {
                Object relationship = entityManager.isRelationship(field) ?
                        buildRelationshipEntityInstance(field, resultSet) : createObject(resultSet, objectClass, field);
                fieldSet(field, instanceClass, relationship);
            }
            return instanceClass;
        } catch (Exception e) {
            throw new ResultSetToObjectMapperException(e);
        }
    }

    private Object createObject(ResultSet resultSet, Class<?> objectClass, Field field) throws SQLException {
        if(field.getDeclaredAnnotation(EnumColumn.class) != null  && field.getType().isEnum())
            return enumInstanceGet(resultSet, objectClass, field);

        return resultSet.getObject(rowGenerate(objectClass, field));
    }

    @SuppressWarnings("all")
    private Object enumInstanceGet(ResultSet resultSet, Class<?> objectClass, Field field) throws SQLException {
        String result = resultSet.getString(rowGenerate(objectClass, field));

        if(field.getDeclaredAnnotation(EnumColumn.class).valueType().equals(EnumValueType.NUMERIC))
            return field.getType().getEnumConstants()[Integer.parseInt(result) - 1];
        return Enum.valueOf((Class<Enum>) field.getType(), result);
    }

    private Object buildRelationshipEntityInstance(Field field, ResultSet fieldValues) throws InstantiationException, IllegalAccessException, SQLException, NoSuchMethodException, InvocationTargetException {
        Object object = field.getType().getDeclaredConstructor().newInstance();
        for (Field idField : entityManager.isIdField(object.getClass()))
            idField.set(object, fieldValues.getObject(rowGenerate(field.getDeclaringClass(), field)));

        return object;
    }

    private void fieldSet(Field field, Object instance, Object relationship) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(instance, relationship);
    }

    private String rowGenerate(Class<?> entity, Field field){
        return entity.getAnnotation(Table.class).name() + EntityCollector.COLUMN_NAME_SEPARATOR + field.getAnnotation(Column.class).name();
    }

}
