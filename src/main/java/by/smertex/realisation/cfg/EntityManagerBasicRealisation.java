package by.smertex.realisation.cfg;

import by.smertex.interfaces.cfg.EntityManager;

import java.util.HashMap;
import java.util.Map;

public class EntityManagerBasicRealisation implements EntityManager {

    private static final EntityManagerBasicRealisation INSTANCE = new EntityManagerBasicRealisation();

    private Map<Class<?>, Object> entities = new HashMap<>();

    private void init(){

    }

    @Override
    public Object getEntity(Class<?> key) {
        return entities.get(key);
    }

    public static EntityManagerBasicRealisation getInstance() {
        return INSTANCE;
    }

    private EntityManagerBasicRealisation(){
        init();
    }
}
