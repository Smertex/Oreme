package by.smertex.interfaces.application.builders;

import java.lang.reflect.Method;
import java.sql.ResultSet;

public interface InstanceBuilder extends EntityCollector {
    Object buildInstance(Class<?> clazz, ResultSet fieldValues);
}
