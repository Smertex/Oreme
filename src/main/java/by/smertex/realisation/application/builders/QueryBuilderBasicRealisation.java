package by.smertex.realisation.application.builders;

import by.smertex.interfaces.cfg.EntityManager;


public class QueryBuilderBasicRealisation extends SelectQueryBuilderBasicRealisation {

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

    public QueryBuilderBasicRealisation(EntityManager entityManager) {
        super(entityManager);
    }
}
