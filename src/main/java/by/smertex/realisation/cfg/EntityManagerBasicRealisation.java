package by.smertex.realisation.cfg;

import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.interfaces.loaders.XmlElementLoader;
import by.smertex.interfaces.mapper.Mapper;
import by.smertex.realisation.loaders.XmlElementLoaderBasicRealisation;
import by.smertex.realisation.mappers.XmlElementToSetEntityClassesMapper;
import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.Set;

public class EntityManagerBasicRealisation implements EntityManager {

    private static final EntityManagerBasicRealisation INSTANCE = new EntityManagerBasicRealisation();

    private final XmlElementLoader xmlElementLoader = XmlElementLoaderBasicRealisation.getInstance();

    private final Mapper<Set<String>, Node> mapper = XmlElementToSetEntityClassesMapper.getInstance();

    private final Set<Class<?>> entities = new HashSet<>();

    private void init(){
        initSetEntities();
    }

    private void initSetEntities(){
        Set<String> classes = mapper.mapFrom(xmlElementLoader.getNodeByTag(EntityManager.XML_ENTITIES_TAG));
        for(String stringClass: classes){
            Class<?> clazz = stringToClass(stringClass);
            validationEntity(clazz);
            entities.add(clazz);
        }
    }

    @Override
    public Boolean isEntity(Class<?> entity) {
        return entities.contains(entity);
    }

    public static EntityManagerBasicRealisation getInstance() {
        return INSTANCE;
    }

    private EntityManagerBasicRealisation(){
        init();
    }
}
