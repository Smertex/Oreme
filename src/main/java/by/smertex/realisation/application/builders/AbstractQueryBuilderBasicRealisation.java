package by.smertex.realisation.application.builders;

import by.smertex.annotation.entity.classes.Table;
import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.annotation.entity.fields.columns.Id;
import by.smertex.exceptions.application.QueryBuilderException;
import by.smertex.interfaces.application.builders.QueryBuilder;
import by.smertex.interfaces.application.session.CompositeKey;
import by.smertex.interfaces.cfg.EntityManager;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractQueryBuilderBasicRealisation implements QueryBuilder {

    protected final EntityManager entityManager;

    protected String fromFieldSql(Class<?> entity) {
        return FROM_SQL.formatted(entityToFromSql(entity));
    }

    private String entityToFromSql(Class<?> entity){
        return Stream.of(entity.getDeclaredAnnotation(Table.class))
                .map(annotation -> tableNameCreate(annotation) + " " + annotation.name())
                .collect(Collectors.joining());
    }

    private String tableNameCreate(Object tableAnnotation){
        if(tableAnnotation instanceof Table)
            return concatPoint(((Table) tableAnnotation).schema(),
                    ((Table) tableAnnotation).name());
        throw new QueryBuilderException(new RuntimeException());
    }

    protected String generatedWhereSql(Class<?> entity, Object id) {
        Field[] field = fieldIdCollector(entity);
        if(field.length != 1) throw new QueryBuilderException(new RuntimeException());
        return equalityGenerate(concatPoint(entity.getAnnotation(Table.class).name(), field[0].getName()),
                id.toString());
    }

    protected String generateWhereSqlWithCompositeKey(Class<?> entity, CompositeKey compositeKey){
        return Arrays.stream(fieldIdCollector(entity))
                .filter(column -> compositeKey.getValue(column.getDeclaredAnnotation(Column.class).name()) != null)
                .map(column -> equalityGenerate(
                        concatPoint(entity.getDeclaredAnnotation(Table.class).name(),
                                column.getAnnotation(Column.class).name()),
                        compositeKey.getValue(column.getDeclaredAnnotation(Column.class).name()).toString())
                )
                .collect(Collectors.joining(AND_SQL));
    }

    protected String equalityGenerate(String el1, String el2){
        return el1 + " = " + el2;
    }

    protected String concatPoint(String el1, String el2){
        return el1 + "." + el2;
    }

    protected Boolean isFieldId(Field field){
        return field.getAnnotation(Id.class) != null;
    }

    protected Field[] fieldIdCollector(Class<?> entity){
        return entityManager.getClassFields(entity).stream()
                .filter(this::isFieldId)
                .toArray(Field[]::new);
    }

    public AbstractQueryBuilderBasicRealisation(EntityManager entityManager){
        this.entityManager = entityManager;
    }
}
