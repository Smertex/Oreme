package by.smertex.realisation.application.session;

import by.smertex.exceptions.application.TransactionException;
import by.smertex.interfaces.application.session.Session;
import by.smertex.interfaces.application.session.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionBasicRealisation implements Transaction {

    private final Session session;

    private final Connection connection;

    private final List<Object> entities;

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

    @Override
    public List<Object> getAllEntitiesInThisTransaction() {
        return null;
    }

    @Override
    public List<Object> getAllEntitiesByType(Class<?> clazz) {
        return entities.stream()
                .filter(entities -> clazz.isAssignableFrom(entities.getClass()))
                .collect(Collectors.toList());
    }

    @Override
    public void addEntityInTransaction(Object entity) {
        entities.add(entity);
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
        this.entities = new ArrayList<>();
    }
}
