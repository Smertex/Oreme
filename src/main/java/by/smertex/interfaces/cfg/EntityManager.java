package by.smertex.interfaces.cfg;

public interface EntityManager {

    String XML_ENTITIES_TAG = "entities";

    Object getEntity(Class<?> key);
}
