package by.smertex.annotation.entity.fields.columns;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumColumn {
    EnumValueType valueType() default EnumValueType.STRING;
}
