package by.smertex.interfaces.application.builders;

import by.smertex.annotation.entity.fields.communications.ManyToOne;
import by.smertex.annotation.entity.fields.communications.OneToOne;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

public interface EntityCollector {

    String COLUMN_NAME_SEPARATOR = "_";

    default Boolean fieldHaveAnnotationRelationship(Field field){
        return Arrays.stream(field.getAnnotations())
                .map(Annotation::annotationType)
                .filter(type -> type == ManyToOne.class || type == OneToOne.class)
                .count() == 1;
    }
}
