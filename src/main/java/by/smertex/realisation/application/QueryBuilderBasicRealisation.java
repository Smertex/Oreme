package by.smertex.realisation.application;

import by.smertex.annotation.entity.classes.Table;
import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.exceptions.application.QueryBuilderException;
import by.smertex.interfaces.application.QueryBuilder;
import by.smertex.interfaces.cfg.EntityManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryBuilderBasicRealisation implements QueryBuilder{

    private final EntityManager entityManager;

    public String selectFieldsSql(Class<?> entity){
        return SELECT_SQL.formatted(entityToSelect(entity)) + "\n"
                + fromFieldsSql(entity) + "\n"
                + joinsGenerate(entity);
    }

    private String fromFieldsSql(Class<?> entity) {
        return FROM_SQL.formatted(entityToFromSql(entity));
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
        String columnName = field.getDeclaringClass().getDeclaredAnnotation(Table.class).name() + "." + field.getDeclaredAnnotation(Column.class).name();
        return AS_SQL.formatted(columnName, columnName.replace(".", "_"));
    }

    private String entityToFromSql(Class<?> entity){
       return Stream.of(entity.getDeclaredAnnotation(Table.class))
                .map(annotation -> tableNameCreate(annotation) + " " + annotation.name())
                .collect(Collectors.joining());
    }

    private String joinsGenerate(Class<?> entity){
        return entityManager.getClassFields(entity).stream()
                .filter(this::fieldHaveAnnotationRelationship)
                .map(field -> createJoin(entity, field))
                .collect(Collectors.joining("\n"));
    }

    private String createJoin(Class<?> entity, Field field){
        return JOIN_SQL.formatted(tableNameCreate(field.getType().getAnnotation(Table.class)),
                field.getType().getAnnotation(Table.class).name() + "." + annotationToMappedByString(field) + "=" + entity.getAnnotation(Table.class).name() + "." + field.getAnnotation(Column.class).name());
    }

    private String tableNameCreate(Object tableAnnotation){
        if(tableAnnotation instanceof Table)
            return ((Table) tableAnnotation).schema() + "." + ((Table) tableAnnotation).name();
        throw new QueryBuilderException(new RuntimeException());
    }


    private String annotationToMappedByString(Field field){
        for(Annotation annotation: field.getAnnotations()){
            try {
                return (String) annotation.annotationType().getDeclaredMethod("mappedBy").invoke(annotation);
            } catch (NoSuchMethodException ignored){
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new QueryBuilderException(e);
            }
        }
        throw new QueryBuilderException(new RuntimeException());
    }


    protected QueryBuilderBasicRealisation(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
