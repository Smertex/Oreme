package by.smertex.interfaces.application;

import java.util.List;
import java.util.Optional;

public interface Crud {

    Optional<Object> find(Class<?> clazz, Long id);

    List<Object> findAll();

    boolean update(Object entity);

    Object save(Object entity);

    boolean delete();

}
