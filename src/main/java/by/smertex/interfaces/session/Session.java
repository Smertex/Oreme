package by.smertex.interfaces.session;

import by.smertex.exceptions.session.SessionException;

import java.io.Closeable;
import java.util.List;
import java.util.Optional;

public interface Session extends Closeable {
    void beginTransaction();

    Transaction getTransaction();

    Optional<Object> find(Class<?> clazz, Long id);

    List<Object> findAll();

    boolean update(Object entity);

    Object save(Object entity);

    boolean delete();

    @Override
    void close() throws SessionException;
}
