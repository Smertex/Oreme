package by.smertex.interfaces.application.session;

import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.exceptions.application.SessionException;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.realisation.application.session.CompositeKeyBasicRealisation;
import by.smertex.realisation.elements.IsolationLevel;
import javassist.util.proxy.ProxyFactory;

import java.io.Closeable;
import java.lang.reflect.Field;

public interface Session extends Closeable, Crud {

    void beginTransaction();

    Transaction getTransaction();

    void setIsolationLevel(IsolationLevel level);

    Object find(Class<?> entity, CompositeKey compositeKey);

    Boolean isClose();

    static CompositeKey listIdToCompositeKey(EntityManager entityManager, Object object) {
        CompositeKey compositeKey = new CompositeKeyBasicRealisation(object.getClass());
        for(Field field: entityManager.getIdField(object.getClass())) {
            try {
                compositeKey.setValue(field.getDeclaredAnnotation(Column.class).name(), field.get(object));
            } catch (IllegalAccessException e) {
                throw new SessionException(e);
            }
        }
        return compositeKey;
    }
}
