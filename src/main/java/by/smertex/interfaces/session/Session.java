package by.smertex.interfaces.session;

import by.smertex.realisation.elements.IsolationLevel;

import java.io.Closeable;

public interface Session extends Closeable, Crud {

    void beginTransaction();

    Transaction getTransaction();

    void setIsolationLevel(IsolationLevel level);

}
