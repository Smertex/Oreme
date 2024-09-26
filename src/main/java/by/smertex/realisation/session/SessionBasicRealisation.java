package by.smertex.realisation.session;

import by.smertex.exceptions.session.SessionException;
import by.smertex.interfaces.session.Session;
import by.smertex.interfaces.session.Transaction;
import by.smertex.realisation.elements.CrudTemplate;

import java.util.List;
import java.util.Optional;

public class SessionBasicRealisation implements Session {

    private final CrudTemplate crudTemplate;

    private final Object entity;

    private Transaction transaction;

    @Override
    public void beginTransaction() {
        this.transaction = new TransactionBasicRealisation();
        //this.transaction.begin();
    }

    @Override
    public Transaction getTransaction() {
        return null;
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
    public void close() throws SessionException {

    }

    public SessionBasicRealisation(Object entity){
        this.crudTemplate = new CrudTemplate();
        this.entity = entity;
    }
}
