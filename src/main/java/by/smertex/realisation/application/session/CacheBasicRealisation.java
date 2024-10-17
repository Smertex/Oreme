package by.smertex.realisation.application.session;

import by.smertex.interfaces.application.session.Cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CacheBasicRealisation implements Cache {

    private final List<Object> cache;

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public void addEntityInCache(Object entity) {
        cache.add(entity);
    }

    @Override
    public List<Object> getEntityInCache() {
        return cache;
    }

    @Override
    public List<Object> getEntityInCacheByType(Class<?> clazz) {
        return cache.stream()
                .filter(entity -> entity.getClass().equals(clazz))
                .collect(Collectors.toList());
    }

    public CacheBasicRealisation(){
        cache = new ArrayList<>();
    }

}
