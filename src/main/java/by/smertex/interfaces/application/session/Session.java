package by.smertex.interfaces.application.session;

import by.smertex.exceptions.application.SessionException;
import by.smertex.realisation.elements.IsolationLevel;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Session extends Closeable, Crud {

    void beginTransaction();

    Transaction getTransaction();

    void setIsolationLevel(IsolationLevel level);

    Object find(Class<?> entity, CompositeKey compositeKey);

    Boolean isClose();
}
