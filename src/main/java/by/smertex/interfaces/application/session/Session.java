package by.smertex.interfaces.application.session;

import by.smertex.realisation.elements.IsolationLevel;

import java.io.Closeable;
import java.util.Optional;

public interface Session extends Closeable, Crud {

    void beginTransaction();

    Transaction getTransaction();

    void setIsolationLevel(IsolationLevel level);

    Optional<Object> find(Class<?> entity, CompositeKey compositeKey);
}
