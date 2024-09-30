package by.smertex.realisation.cfg;

import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.interfaces.loaders.XmlElementLoader;
import by.smertex.interfaces.mapper.Mapper;
import by.smertex.realisation.loaders.XmlElementLoaderBasicRealisation;
import by.smertex.realisation.mappers.XmlElementToSetEntityClassesMapper;
import org.w3c.dom.Node;

import java.lang.reflect.Field;
import java.util.*;

public class EntityManagerBasicRealisation implements EntityManager {

    private static final EntityManagerBasicRealisation INSTANCE = new EntityManagerBasicRealisation();

    private final XmlElementLoader xmlElementLoader = XmlElementLoaderBasicRealisation.getInstance();

    private final Mapper<Set<String>, Node> mapper = XmlElementToSetEntityClassesMapper.getInstance();

    private final Map<Class<?>, List<Field>> entities = new HashMap<>();

    private void init(){
        initSetEntities();
    }

    private void initSetEntities(){
        Set<String> classes = mapper.mapFrom(xmlElementLoader.getNodeByTag(EntityManager.XML_ENTITIES_TAG));
        for(String stringClass: classes){
            Class<?> clazz = stringToClass(stringClass);
            validationEntity(clazz);
            entities.put(clazz, createListFields(clazz));
        }
    }

    @Override
    public List<Field> getClassFields(Class<?> key) {
        return entities.get(key);
    }

    private List<Field> createListFields(Class<?> clazz){
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getDeclaredAnnotation(Column.class) != null)
                .toList();
    }

    public static EntityManagerBasicRealisation getInstance() {
        return INSTANCE;
    }

    private EntityManagerBasicRealisation(){
        init();
    }
}
