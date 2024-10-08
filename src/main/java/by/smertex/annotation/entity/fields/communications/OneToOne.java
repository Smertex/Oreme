package by.smertex.annotation.entity.fields.communications;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToOne {
    String mappedBy();

    QueryStrategy strategy() default QueryStrategy.EAGER;
}
