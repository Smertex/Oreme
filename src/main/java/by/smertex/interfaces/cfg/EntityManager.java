package by.smertex.interfaces.cfg;


import by.smertex.annotation.entity.classes.Entity;
import by.smertex.annotation.entity.classes.Table;
import by.smertex.exceptions.cfg.ClassNotEntity;
import by.smertex.exceptions.cfg.ClassNotFound;

import java.lang.reflect.Field;
import java.util.List;

public interface EntityManager {

    String XML_ENTITIES_TAG = "entities";

    List<Field> getClassFields(Class<?> key);

    Boolean fieldHaveToManyAnnotation(Field key);

    Boolean isLazyRelationship(Field key);

    Boolean isRelationship(Field key);

    List<Field> getIdField(Class<?> entity);

    String getRelationshipMappedBy(Field field);

    Field getFieldIdByKeyFor(Class<?> entity, String relationship);

    Class<?> getGenericTypeInRelationshipCollection(Field field);

    List<Field> getToManyRelationship(Class<?> entity);

    default void validationEntity(Class<?> clazz){
        if(clazz.getDeclaredAnnotation(Entity.class) == null || clazz.getDeclaredAnnotation(Table.class) == null)
            throw new ClassNotEntity(new RuntimeException());
    }

    default Class<?> stringToClass(String classPath){
        try {
            return Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFound(new RuntimeException());
        }
    }
}
