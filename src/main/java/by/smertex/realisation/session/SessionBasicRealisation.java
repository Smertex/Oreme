package by.smertex.realisation.session;

import by.smertex.exceptions.session.SessionException;
import by.smertex.interfaces.session.QueryBuilder;
import by.smertex.interfaces.session.Session;
import by.smertex.interfaces.session.Transaction;
import by.smertex.realisation.elements.IsolationLevel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SessionBasicRealisation implements Session {
    private final Connection connection;

    private final QueryBuilder queryBuilder;

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
        return queryBuilder.find(clazz, id);
    }

    @Override
    public List<Object> findAll() {
        return queryBuilder.findAll();
    }

    @Override
    public boolean update(Object entity) {
        return queryBuilder.update(entity);
    }

    @Override
    public Object save(Object entity) {
        return queryBuilder.save(entity);
    }

    @Override
    public boolean delete() {
        return queryBuilder.delete();
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

    protected SessionBasicRealisation(Connection connection, IsolationLevel level){
        this.connection = connection;
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new SessionException(e);
        }
        setIsolationLevel(level);
        this.queryBuilder = QueryBuilderBasicRealisation.getInstance();
    }
}
