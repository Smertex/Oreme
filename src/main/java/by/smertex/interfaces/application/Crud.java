package by.smertex.interfaces.application;

import java.util.List;
import java.util.Optional;

public interface Crud {

    Optional<Object> find(Class<?> entity, Long id);

    List<Object> findAll(Class<?> entity);

    boolean update(Object entity);

    Object save(Object entity);

    boolean delete(Object entity);

}
