package by.smertex.realisation.cfg;

import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.annotation.entity.fields.columns.Id;
import by.smertex.annotation.entity.fields.communications.*;
import by.smertex.exceptions.cfg.EntityManagerException;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.interfaces.loaders.XmlElementLoader;
import by.smertex.interfaces.mapper.Mapper;
import org.w3c.dom.Node;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public class EntityManagerBasicRealisation implements EntityManager {

    private final XmlElementLoader xmlElementLoader;

    private final Mapper<Set<String>, Node> mapper;

    private final Map<Class<?>, List<Field>> entities;

    private final Map<Field, Annotation> relationshipField;

    private void init(){
        initSetEntities();
    }

    private void initSetEntities(){
        Set<String> classes = mapper.mapFrom(xmlElementLoader.getNodeByTag(EntityManager.XML_ENTITIES_TAG));
        for(String stringClass: classes){
            Class<?> clazz = stringToClass(stringClass);
            List<Field> fields = List.of(clazz.getDeclaredFields());
            validationEntity(clazz);
            iterationByFields(fields);
            entities.put(clazz, createListFields(fields));
        }
    }

    private void iterationByFields(List<Field> fields){
        fields.forEach(this::addRelationship);
    }

    private void addRelationship(Field field){
        Annotation[] annotations = Arrays.stream(field.getDeclaredAnnotations())
                .filter(el -> el.annotationType() == ManyToOne.class
                        || el.annotationType() == OneToOne.class
                        || el.annotationType() == ManyToMany.class
                        || el.annotationType() == OneToMany.class )
                .toArray(Annotation[]::new);
        if(annotations.length > 1) throw new EntityManagerException(new RuntimeException());
        if(annotations.length == 1) relationshipField.put(field, annotations[0]);
    }

    @Override
    public List<Field> getClassFields(Class<?> key) {
        return entities.get(key);
    }

    @Override
    public Boolean fieldHaveToManyAnnotation(Field key) {
        Annotation annotation = relationshipField.get(key);
        return annotation instanceof OneToMany || annotation instanceof ManyToMany;
    }


    @Override
    public Boolean isLazyRelationship(Field key) {
        Annotation annotation = relationshipField.get(key);
        if (annotation instanceof ManyToOne)
            return ((ManyToOne) annotation).strategy().equals(QueryStrategy.LAZY);
        if (annotation instanceof OneToOne)
            return ((OneToOne) annotation).strategy().equals(QueryStrategy.LAZY);
        if (annotation instanceof OneToMany)
            return ((OneToMany) annotation).strategy().equals(QueryStrategy.LAZY);
        if (annotation instanceof ManyToMany)
            return ((ManyToMany) annotation).strategy().equals(QueryStrategy.LAZY);
        return false;
    }

    @Override
    public Boolean isRelationship(Field key) {
        return relationshipField.get(key) != null;
    }

    @Override
    public List<Field> getIdField(Class<?> entity) {
        return entities.get(entity).stream()
                .filter(field -> field.getDeclaredAnnotation(Id.class) != null)
                .collect(Collectors.toList());
    }

    @Override
    public String getRelationshipMappedBy(Field key) {
        Annotation annotation = relationshipField.get(key);
        if (annotation instanceof ManyToOne)
            return ((ManyToOne) annotation).mappedBy();
        if (annotation instanceof OneToOne)
            return ((OneToOne) annotation).mappedBy();
        if (annotation instanceof ManyToMany)
            return ((ManyToMany) annotation).mappedBy();
        if (annotation instanceof OneToMany)
            return ((OneToMany) annotation).mappedBy();

        return null;
    }

    @Override
    public Field getFieldIdByKeyFor(Class<?> entity, String relationship) {
        for(Field fieldId: getIdField(entity)){
            Id annotation = fieldId.getDeclaredAnnotation(Id.class);
            for(int i = 0; i < annotation.keyFor().length; i++){
                if(annotation.keyFor()[i].equals(relationship)){
                    return fieldId;
                }
            }
        }
        return null;
    }

    @Override
    public Class<?> getGenericTypeInRelationshipCollection(Field field) {
        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

    private List<Field> createListFields(List<Field> fields){
        return fields.stream()
                .filter(field -> field.getDeclaredAnnotation(Column.class) != null)
                .peek(field -> field.setAccessible(true))
                .toList();
    }

    protected EntityManagerBasicRealisation(XmlElementLoader xmlElementLoader,
                                         Mapper<Set<String>, Node> mapper){
        this.xmlElementLoader = xmlElementLoader;
        this.mapper = mapper;
        entities = new HashMap<>();
        relationshipField = new HashMap<>();
        init();
    }
}
