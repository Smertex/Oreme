package by.smertex.realisation.application;

import by.smertex.annotation.entity.classes.Table;
import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.interfaces.application.QueryBuilder;
import by.smertex.interfaces.cfg.EntityManager;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryBuilderBasicRealisation implements QueryBuilder{

    private final EntityManager entityManager;

    public String selectFieldsSql(Class<?> entity){
        return SELECT_SQL.formatted(entityManager.getClassFields(entity).stream()
                .map(field -> field.getDeclaredAnnotation(Column.class).name())
                .collect(Collectors.joining(", ")));
    }

    public String fromFieldsSql(Class<?> entity){
        return FROM_SQL.formatted(Stream.of(entity.getDeclaredAnnotation(Table.class))
                .map(annotation -> annotation.schema() + "." + annotation.name())
                .collect(Collectors.joining()));
    }

    public QueryBuilderBasicRealisation(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
