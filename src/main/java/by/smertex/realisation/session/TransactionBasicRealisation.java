package by.smertex.realisation.session;

import by.smertex.interfaces.session.Transaction;
import by.smertex.realisation.elements.IsolationLevel;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionBasicRealisation implements Transaction {

    private Connection connection;

    @Override
    public void begin(){
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
        connection.close();
    }

    @Override
    public void setIsolationLevel(IsolationLevel level) throws SQLException {
        connection.setTransactionIsolation(level.getLevel());
    }

    protected TransactionBasicRealisation(){

    }
}
