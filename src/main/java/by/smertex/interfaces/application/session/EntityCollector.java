package by.smertex.interfaces.application.session;

import by.smertex.annotation.entity.fields.communications.Relationship;

import java.lang.reflect.Field;
import java.util.Arrays;

public interface EntityCollector {

    String COLUMN_NAME_SEPARATOR = "_";

    default Boolean fieldHaveAnnotationRelationship(Field field){
        return Arrays.stream(field.getAnnotations())
                .filter(annotation -> annotation.annotationType().getAnnotation(Relationship.class) != null)
                .count() == 1;
    }
}
