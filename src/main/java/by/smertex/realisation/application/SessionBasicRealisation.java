package by.smertex.realisation.application;

import by.smertex.exceptions.application.SessionException;
import by.smertex.interfaces.application.Session;
import by.smertex.interfaces.application.Transaction;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.realisation.elements.IsolationLevel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SessionBasicRealisation implements Session {
    private final Connection connection;

    private final EntityManager entityManager;

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

    protected SessionBasicRealisation(EntityManager entityManager, Connection connection, IsolationLevel level){
        this.connection = connection;
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new SessionException(e);
        }
        setIsolationLevel(level);
        this.entityManager = entityManager;
    }
}
