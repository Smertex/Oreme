package by.smertex.realisation.mappers;

import by.smertex.annotation.entity.classes.Table;
import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.annotation.entity.fields.columns.EnumColumn;
import by.smertex.annotation.entity.fields.columns.EnumValueType;
import by.smertex.interfaces.application.builders.EntityCollector;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractResultSetToObjectMapper {
    protected Object createObject(ResultSet resultSet, Class<?> objectClass, Field field) throws SQLException {
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

    protected String rowGenerate(Class<?> entity, Field field){
        return entity.getAnnotation(Table.class).name() + EntityCollector.COLUMN_NAME_SEPARATOR + field.getAnnotation(Column.class).name();
    }
}
