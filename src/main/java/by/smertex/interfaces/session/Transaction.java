package by.smertex.interfaces.session;

import by.smertex.realisation.elements.IsolationLevel;

import java.sql.Connection;
import java.sql.SQLException;

public interface Transaction {
    void begin() throws SQLException;

    void commit() throws SQLException;

    void setIsolationLevel(IsolationLevel level) throws SQLException;
}
