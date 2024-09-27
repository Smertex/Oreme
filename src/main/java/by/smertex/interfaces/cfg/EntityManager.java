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
