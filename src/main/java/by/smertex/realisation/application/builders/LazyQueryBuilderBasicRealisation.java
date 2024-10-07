package by.smertex.realisation.application.builders;

import by.smertex.interfaces.cfg.EntityManager;

public class LazyQueryBuilderBasicRealisation extends LazySelectQueryBuilderBasicRealisation{
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

    public LazyQueryBuilderBasicRealisation(EntityManager entityManager){
        super(entityManager);
    }
}
