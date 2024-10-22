package by.smertex.realisation.application.builders;

import by.smertex.interfaces.application.session.CompositeKey;
import by.smertex.interfaces.cfg.EntityManager;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public abstract class SelectQueryBuilder
        extends AbstractQueryBuilderBasicRealisation {
    public SelectQueryBuilder(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public String selectSql(Class<?> entity) {
        return SELECT_SQL.formatted(selectGenerate(entity)) + fromFieldSql(entity);
    }

    @Override
    public String selectSql(Class<?> entity, CompositeKey compositeKey) {
        return selectSql(entity) + WHERE_SQL.formatted(generateWhereSqlWithCompositeKey(entity, compositeKey));
    }

    private String selectGenerate(Class<?> entity){
        return entityManager.getClassFields(entity).stream()
                .map(this::appendFieldToSelect)
                .collect(Collectors.joining(",\n\t"));
    }

    private String appendFieldToSelect(Field field){
        return columnNameGenerate(field);
    }
}
