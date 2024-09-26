package by.smertex.interfaces.session;

import by.smertex.realisation.elements.IsolationLevel;

public interface Transaction {
    void begin(java.sql.Connection connection, IsolationLevel level);

    void commit();

    void setIsolationLevel(IsolationLevel level);

    java.sql.Connection getConnection();
}
