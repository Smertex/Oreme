package by.smertex.interfaces.application.builders;

import by.smertex.interfaces.application.session.CompositeKey;

public interface ProxyEntityQueryBuilder extends QueryBuilder, EntityCollector{

    String AS_SQL = "%s AS %s";

    String JOIN_SQL = "JOIN %s ON %s \n";

    String selectWhereSql(Class<?> entity, Long id);

    String selectWhereSql(Class<?> entity, CompositeKey compositeKey);

}
