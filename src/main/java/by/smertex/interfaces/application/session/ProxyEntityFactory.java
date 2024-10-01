package by.smertex.interfaces.application.session;

public interface ProxyEntityFactory {

    ProxyEntity buildProxyEntity(Class<?> entity, Object id);

}
