package by.smertex.interfaces.application.session;

public interface LazyInitializer {

    Object initialize(Object object);

    Session getSession();

    ProxyEntityBuilder getProxyEntityBuilder();
}
