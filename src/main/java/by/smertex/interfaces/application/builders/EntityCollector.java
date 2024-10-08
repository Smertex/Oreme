package by.smertex.interfaces.application.builders;

import by.smertex.annotation.entity.fields.communications.ManyToOne;
import by.smertex.annotation.entity.fields.communications.OneToOne;
import by.smertex.annotation.entity.fields.communications.QueryStrategy;

import java.lang.annotation.Annotation;

public interface EntityCollector {
    String COLUMN_NAME_SEPARATOR = "_";

    default Boolean isFieldIsJoin(Annotation annotation) {
        if (annotation instanceof ManyToOne)
            return ((ManyToOne) annotation).strategy().equals(QueryStrategy.EAGER);
        if (annotation instanceof OneToOne)
            return ((OneToOne) annotation).strategy().equals(QueryStrategy.EAGER);
        return false;
    }
}
