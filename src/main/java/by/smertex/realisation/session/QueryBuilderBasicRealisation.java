package by.smertex.realisation.session;

import by.smertex.interfaces.session.QueryBuilder;

import java.util.List;
import java.util.Optional;

public class QueryBuilderBasicRealisation implements QueryBuilder {

    private static final QueryBuilderBasicRealisation INSTANCE = new QueryBuilderBasicRealisation();

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

    public static QueryBuilderBasicRealisation getInstance() {
        return INSTANCE;
    }

    protected QueryBuilderBasicRealisation(){

    }
}
