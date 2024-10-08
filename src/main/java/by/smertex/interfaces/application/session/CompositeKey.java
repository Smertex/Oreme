package by.smertex.interfaces.application.session;

import java.util.Map;

public interface CompositeKey {
    Map<String, Object> getKeys();

    Object getValue(String key);

    void setValue(String column, Object value);
}
