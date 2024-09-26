package by.smertex.interfaces.session;

import by.smertex.realisation.elements.IsolationLevel;

import java.io.Closeable;
import java.util.List;
import java.util.Optional;

public interface Session extends Closeable {
    String SELECT = "SELECT %s FROM %s";

    String WHERE = "WHERE %s";

    String JOIN = "JOIN %s ON %s";

    void beginTransaction();

    Transaction getTransaction();

    Optional<Object> find(Class<?> clazz, Long id);

    List<Object> findAll();

    boolean update(Object entity);

    Object save(Object entity);

    boolean delete();

    void setIsolationLevel(IsolationLevel level);

    @Override
    void close();
}
