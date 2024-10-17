package by.smertex.interfaces.application.session;

public interface LazyInitializerFactory {
    LazyInitializer createLazyInitializer(Session session);
}
