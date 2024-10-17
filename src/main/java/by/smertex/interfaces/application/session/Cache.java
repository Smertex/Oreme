package by.smertex.interfaces.application.session;

import java.util.List;

public interface Cache {

    void clear();

    void addEntityInCache(Class<?> clazz, Object entity, CompositeKey compositeKey);

    List<Object> getEntityInCache();

    List<Object> getEntityInCacheByType(Class<?> clazz);

    Object getEntity(Class<?> clazz, CompositeKey compositeKey);

}
