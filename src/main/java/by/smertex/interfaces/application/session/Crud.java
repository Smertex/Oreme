package by.smertex.interfaces.application.session;

import java.util.List;
import java.util.Optional;

public interface Crud {

    Object find(Class<?> entity, Long id);

    List<Object> findAll(Class<?> entity);

    boolean update(Object entity);

    Object save(Object entity);

    boolean delete(Object entity);

}
