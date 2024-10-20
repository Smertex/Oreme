package by.smertex.realisation.mappers;

import by.smertex.exceptions.mapper.ResultSetToObjectMapperException;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.interfaces.mapper.ResultSetToObjectMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ResultSetToObjectMapperForNoJoin
        extends AbstractResultSetToObjectMapper implements ResultSetToObjectMapper {
    private final EntityManager entityManager;

    public ResultSetToObjectMapperForNoJoin(EntityManager entityManager){
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

}
