package by.smertex.realisation.application.builders;

import by.smertex.annotation.entity.classes.Table;
import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.interfaces.application.builders.EntityCollector;
import by.smertex.interfaces.application.builders.QueryBuilder;
import by.smertex.interfaces.application.session.CompositeKey;
import by.smertex.interfaces.cfg.EntityManager;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public abstract class LazySelectQueryBuilderBasicRealisation extends AbstractQueryBuilderBasicRealisation implements QueryBuilder, EntityCollector {

    @Override
    public String selectSql(Class<?> entity) {
        return SELECT_SQL.formatted(entityToSelect(entity)) + fromFieldSql(entity);
    }

    @Override
    public String selectSql(Class<?> entity, Object id) {
        return selectSql(entity) + generatedWhereSql(entity, id);
    }

    @Override
    public String selectSql(Class<?> entity, CompositeKey compositeKey) {
        return selectSql(entity) + generatedWhereSql(entity, compositeKey);
    }

    private String entityToSelect(Class<?> entity){
        return entityManager.getClassFields(entity).stream()
                .map(this::recursiveQuery)
                .collect(Collectors.joining(",\n\t"));
    }

    private String recursiveQuery(Field field){
        return !fieldHaveAnnotationRelationship(field) ?
                columnNameGenerate(field) : columnNameGenerate(field) + ", " + entityToSelect(field.getType());
    }

    private String columnNameGenerate(Field field){
        String columnName = concatPoint(field.getDeclaringClass().getDeclaredAnnotation(Table.class).name(),
                field.getDeclaredAnnotation(Column.class).name());
        return AS_SQL.formatted(columnName, columnName.replace(".", COLUMN_NAME_SEPARATOR));
    }


    public LazySelectQueryBuilderBasicRealisation(EntityManager entityManager){
        super(entityManager);
    }
}
