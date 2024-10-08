package by.smertex.realisation.application.session;

import by.smertex.interfaces.application.session.Cache;
import by.smertex.interfaces.application.session.CacheFactory;

public class CacheFactoryBasicRealisation implements CacheFactory {
    @Override
    public Cache createCache() {
        return new CacheBasicRealisation();
    }
}
