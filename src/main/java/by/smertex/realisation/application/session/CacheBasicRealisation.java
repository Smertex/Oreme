package by.smertex.realisation.application.session;

import by.smertex.interfaces.application.session.Cache;
import by.smertex.interfaces.application.session.CompositeKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CacheBasicRealisation implements Cache {

    private final Map<Class<?>, Map<CompositeKey, Object>> cacheMap;

    @Override
    public void clear() {
        cacheMap.clear();
    }

    @Override
    public void addEntityInCache(Class<?> clazz, Object entity, CompositeKey compositeKey) {
        cacheMap.computeIfAbsent(clazz, k -> new HashMap<>());
        cacheMap.get(clazz).put(compositeKey, entity);
    }

    @Override
    public List<Object> getEntityInCache() {
        return cacheMap.values().stream()
                .map(Map::values)
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> getEntityInCacheByType(Class<?> clazz) {
        return new ArrayList<>(cacheMap.get(clazz).values());
    }

    @Override
    public Object getEntity(Class<?> clazz, CompositeKey compositeKey) {
        Map<CompositeKey, Object> entities = cacheMap.get(clazz);

        if(entities != null) {
            return entities.get(compositeKey);
        }
        return null;
    }

    public CacheBasicRealisation(){
        cacheMap = new HashMap<>();
    }

}
