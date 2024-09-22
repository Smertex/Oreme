package by.smertex.interfaces.cfg;

import by.smertex.exceptions.cfg.CreateEntityInstanceException;

import java.lang.reflect.InvocationTargetException;

public interface EntityManager {

    String XML_ENTITIES_TAG = "entities";

    Object getEntity(Class<?> key);

    static Object createEntityInstance(Class<?> clazz){
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new CreateEntityInstanceException(e);
        }
    }
}
