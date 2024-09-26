package by.smertex.realisation.session;

import by.smertex.exceptions.session.TransactionException;
import by.smertex.interfaces.session.Transaction;
import by.smertex.realisation.elements.IsolationLevel;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionBasicRealisation implements Transaction {

    private Connection connection;

    @Override
    public void begin(Connection connection, IsolationLevel level) {
        this.connection = connection;
        setIsolationLevel(level);
    }

    @Override
    public void commit(){
        try {
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    @Override
    public void setIsolationLevel(IsolationLevel level) {
        try {
            connection.setTransactionIsolation(level.getLevel());
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
