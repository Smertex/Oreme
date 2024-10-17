package by.smertex.interfaces.mapper;

import java.sql.ResultSet;

public interface ResultSetToObjectMapper {
    Object map(Class<?> objectClass, ResultSet resultSet);
}
