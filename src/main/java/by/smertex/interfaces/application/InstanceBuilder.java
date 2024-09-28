package by.smertex.interfaces.application;

import java.sql.ResultSet;

public interface InstanceBuilder {
    Object buildInstance(Class<?> clazz, ResultSet fieldValues);
}
