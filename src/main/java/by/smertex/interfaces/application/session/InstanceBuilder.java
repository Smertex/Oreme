package by.smertex.interfaces.application.session;

import by.smertex.interfaces.application.session.EntityCollector;

import java.sql.ResultSet;

public interface InstanceBuilder extends EntityCollector {
    Object buildInstance(Class<?> clazz, ResultSet fieldValues);
}
