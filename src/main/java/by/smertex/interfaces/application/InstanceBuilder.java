package by.smertex.interfaces.application;

import java.sql.ResultSet;

public interface InstanceBuilder extends EntityCollector{
    Object buildInstance(Class<?> clazz, ResultSet fieldValues);
}
