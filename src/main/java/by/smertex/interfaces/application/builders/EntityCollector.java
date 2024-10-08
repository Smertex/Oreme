package by.smertex.interfaces.application.builders;

import by.smertex.annotation.entity.fields.communications.ManyToOne;
import by.smertex.annotation.entity.fields.communications.OneToOne;
import by.smertex.annotation.entity.fields.communications.QueryStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

public interface EntityCollector {

    String COLUMN_NAME_SEPARATOR = "_";

    default Boolean isFieldIsJoin(Field field){
        Annotation[] annotations = Arrays.stream(field.getDeclaredAnnotations())
                .filter(el -> el.annotationType() == ManyToOne.class || el.annotationType() == OneToOne.class)
                .toArray(Annotation[]::new);
        if(annotations.length == 1) {
            if (annotations[0] instanceof ManyToOne)
                return ((ManyToOne) annotations[0]).strategy().equals(QueryStrategy.EAGER);
            if (annotations[0] instanceof OneToOne)
                return ((OneToOne) annotations[0]).strategy().equals(QueryStrategy.EAGER);
        }
        return false;
    }

    default Boolean fieldHaveAnnotationRelationship(Field field){
        return Arrays.stream(field.getAnnotations())
                .map(Annotation::annotationType)
                .filter(type -> type == ManyToOne.class || type == OneToOne.class)
                .count() == 1;
    }
}
