package by.smertex.realisation.session;

import by.smertex.exceptions.session.TransactionException;
import by.smertex.interfaces.session.Session;
import by.smertex.interfaces.session.Transaction;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionBasicRealisation implements Transaction {

    private final Session session;

    private final Connection connection;

    @Override
    public void begin() {
        if(session == null || connection == null)
            throw new TransactionException(new RuntimeException());
    }

    @Override
    public void rollback() {
        begin();
    }

    @Override
    public void commit(){
        try {
            connection.commit();
            deleteThisInstance();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    private void deleteThisInstance(){
        try {
            java.lang.reflect.Field field = session.getClass().getDeclaredField("transaction");
            field.setAccessible(true);
            field.set(session, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new TransactionException(e);
        }
    }

    protected TransactionBasicRealisation(Connection connection, Session session){
        this.connection = connection;
        this.session = session;
    }
}
