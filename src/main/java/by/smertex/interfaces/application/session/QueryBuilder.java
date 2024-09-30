package by.smertex.interfaces.application.session;

public interface QueryBuilder extends EntityCollector {
    String SELECT_SQL = "SELECT %s \n";

    String AS_SQL = "%s AS %s";

    String FROM_SQL = "FROM %s \n";

    String WHERE_SQL = "WHERE %s ";

    String AND_SQL = " AND ";

    String JOIN_SQL = "JOIN %s ON %s \n";

    String UPDATE_SQL = "UPDATE %s SET %s ";

    String SAVE_SQL = "INSERT INTO %s (%s) VALUES (%s)";

    String DELETE_SQL = "DELETE %s ";

    String selectFieldsSql(Class<?> entity);

    String selectWhereSql(Class<?> entity, Long id);

    String selectWhereSql(Class<?> entity, CompositeKey compositeKey);

    String updateSql(Class<?> entity);

    String saveSql(Class<?> entity);

    String deleteSql(Class<?> entity);
}
