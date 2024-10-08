package by.smertex.interfaces.application.session;

import java.util.List;

public interface Cache {

    void clear();

    void addEntityInCache(Object entity);

    List<Object> getEntityInCache();

    List<Object> getEntityInCacheByType(Class<?> clazz);

}
