package by.smertex.realisation.application.builders;

import by.smertex.annotation.entity.classes.Table;
import by.smertex.exceptions.application.QueryBuilderException;
import by.smertex.interfaces.application.builders.QueryBuilder;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractQueryBuilderBasicRealisation implements QueryBuilder {

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

    private String concatPoint(String el1, String el2){
        return el1 + "." + el2;
    }
}