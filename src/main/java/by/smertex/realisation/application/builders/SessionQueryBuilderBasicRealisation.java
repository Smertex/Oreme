package by.smertex.realisation.application.builders;

import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.annotation.entity.fields.columns.Id;
import by.smertex.interfaces.application.builders.SessionQueryBuilder;
import by.smertex.interfaces.cfg.EntityManager;

import java.util.stream.Collectors;

public class SessionQueryBuilderBasicRealisation extends AbstractQueryBuilderBasicRealisation implements SessionQueryBuilder {

    private final EntityManager entityManager;

    @Override
    public String selectSql(Class<?> entity){
        return SELECT_SQL.formatted(selectColumnKeysSql(entity)) + fromFieldSql(entity);
    }

    private String selectColumnKeysSql(Class<?> entity){
        return entityManager.getClassFields(entity).stream()
                .filter(column -> column.getAnnotation(Id.class) != null)
                .map(column -> column.getAnnotation(Column.class).name())
                .collect(Collectors.joining(", "));
    }

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

    public SessionQueryBuilderBasicRealisation(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
