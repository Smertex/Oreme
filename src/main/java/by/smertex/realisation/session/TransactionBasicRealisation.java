package by.smertex.realisation.session;

import by.smertex.exceptions.session.TransactionException;
import by.smertex.interfaces.session.Transaction;
import by.smertex.realisation.elements.IsolationLevel;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionBasicRealisation implements Transaction {

    private final Connection connection;

    protected TransactionBasicRealisation(Connection connection){
        this.connection = connection;
    }

    @Override
    public void begin() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    @Override
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    @Override
    public void commit(){
        try {
            connection.commit();
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

}
