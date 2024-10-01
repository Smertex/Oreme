package by.smertex.realisation.application.session;

import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.annotation.entity.fields.columns.Id;
import by.smertex.exceptions.application.AnnotationColumnNotFound;
import by.smertex.exceptions.application.ColumnNotId;
import by.smertex.interfaces.application.session.CompositeKey;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CompositeKeyBasicRealisation implements CompositeKey {
    private final Map<String, Object> keys = new HashMap<>();

    @Override
    public Map<String, Object> getKeys() {
        return keys;
    }

    @Override
    public Object getValue(String key) {
        return keys.get(key);
    }

    @Override
    public void setValue(String column, Object value) {
        if(!keys.containsKey(column))
            throw new ColumnNotId(new RuntimeException());
        keys.put(column, value);
    }

    public CompositeKeyBasicRealisation(Class<?> entity){
        initMapKeys(entity);
    }

    private void initMapKeys(Class<?> entity){
        try {
            Arrays.stream(entity.getDeclaredFields())
                    .filter(column -> column.getDeclaredAnnotation(Id.class) != null)
                    .forEach(column -> keys.put(column.getDeclaredAnnotation(Column.class).name(), null));
        } catch (RuntimeException e){
            throw new AnnotationColumnNotFound(e);
        }
    }
}
