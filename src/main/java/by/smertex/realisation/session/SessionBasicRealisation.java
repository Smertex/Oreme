package by.smertex.realisation.session;

import by.smertex.exceptions.session.SessionException;
import by.smertex.interfaces.session.Session;
import by.smertex.interfaces.session.Transaction;
import by.smertex.realisation.elements.IsolationLevel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SessionBasicRealisation implements Session {
    private Connection connection;

    private final Class<?> entity;

    private Transaction transaction;

    @Override
    public void beginTransaction() {
        this.transaction = new TransactionBasicRealisation(connection, this);
        this.transaction.begin();
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public void setIsolationLevel(IsolationLevel level) {
        try {
            connection.setTransactionIsolation(level.getLevel());
        } catch (SQLException e) {
            throw new SessionException(e);
        }
    }

    @Override
    public Optional<Object> find(Class<?> clazz, Long id) {
        return Optional.empty();
    }

    @Override
    public List<Object> findAll() {
        return null;
    }

    @Override
    public boolean update(Object entity) {
        return false;
    }

    @Override
    public Object save(Object entity) {
        return null;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public void close() {
        if(transaction != null) transaction.commit();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new SessionException(e);
        }
    }

    public SessionBasicRealisation(Connection connection, Class<?> entity, IsolationLevel level){
        this.connection = connection;
        setIsolationLevel(level);
        this.entity = entity;
    }
}
