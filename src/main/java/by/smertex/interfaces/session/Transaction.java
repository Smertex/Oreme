package by.smertex.interfaces.session;

import by.smertex.realisation.elements.IsolationLevel;

public interface Transaction {
    void begin();

    void rollback();

    void commit();

    void setIsolationLevel(IsolationLevel level);

}
