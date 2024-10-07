package by.smertex.interfaces.application.session;

import java.util.List;

public interface Transaction {
    void begin();

    void rollback();

    void commit();

    List<Object> getAllEntitiesInThisTransaction();

    List<Object> getAllEntitiesByType(Class<?> clazz);

    void addEntityInTransaction(Object entity);
}
