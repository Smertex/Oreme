package by.smertex.realisation.application.builders;

import by.smertex.interfaces.cfg.EntityManager;

public class QueryBuilderForNoJoin extends SelectQueryBuilderForNoJoin{

    @Override
    public String updateSql(Class<?> entity) {
        return null;
    }

    @Override
    public String saveSql(Class<?> entity) {
        return null;
    }

    @Override
    public String deleteSql(Class<?> entity) {
        return null;
    }

    public QueryBuilderForNoJoin(EntityManager entityManager) {
        super(entityManager);
    }
}
