package by.smertex.realisation.application.builders;

import by.smertex.annotation.entity.classes.Table;
import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.exceptions.application.QueryBuilderException;
import by.smertex.interfaces.application.builders.EntityCollector;
import by.smertex.interfaces.application.builders.QueryBuilder;
import by.smertex.interfaces.application.session.CompositeKey;
import by.smertex.interfaces.cfg.EntityManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;

public abstract class SelectQueryBuilderBasicRealisation
        extends AbstractQueryBuilderBasicRealisation implements QueryBuilder {

    @Override
    public String selectSql(Class<?> entity) {
        return SELECT_SQL.formatted(selectGenerate(entity)) + fromFieldSql(entity) + joinsGenerate(entity);
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
        return entityManager.isRelationship(field) ?
                relationshipQueryGenerate(field) : columnNameGenerate(field);
    }

    private String relationshipQueryGenerate(Field field){
        return entityManager.isLazyRelationship(field) ?
                columnNameGenerate(field) : columnNameGenerate(field) + ", \n\t" + selectGenerate(field.getType());
    }

    private String columnNameGenerate(Field field){
        String columnName = concatPoint(field.getDeclaringClass().getDeclaredAnnotation(Table.class).name(),
                field.getDeclaredAnnotation(Column.class).name());
        return AS_SQL.formatted(columnName, columnName.replace(".", EntityCollector.COLUMN_NAME_SEPARATOR));
    }

    private String joinsGenerate(Class<?> entity){
        return entityManager.getClassFields(entity).stream()
                .filter(entityManager::isRelationship)
                .filter(field -> !entityManager.isLazyRelationship(field))
                .map(field -> createJoin(entity, field))
                .collect(Collectors.joining("\n"));
    }

    private String createJoin(Class<?> entity, Field field){
        return JOIN_SQL.formatted(tableNameCreate(field.getType().getAnnotation(Table.class)),
                equalityGenerate(concatPoint(field.getType().getAnnotation(Table.class).name(),
                                             annotationToMappedByString(field)),
                                 concatPoint(entity.getAnnotation(Table.class).name(),
                                             field.getAnnotation(Column.class).name())));
    }

    private String tableNameCreate(Object tableAnnotation){
        if(tableAnnotation instanceof Table)
            return concatPoint(((Table) tableAnnotation).schema(),
                    ((Table) tableAnnotation).name());
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

    protected SelectQueryBuilderBasicRealisation(EntityManager entityManager) {
        super(entityManager);
    }
}
