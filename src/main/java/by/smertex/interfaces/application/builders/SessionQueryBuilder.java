package by.smertex.interfaces.application.builders;

import by.smertex.interfaces.application.session.CompositeKey;

public interface SessionQueryBuilder extends QueryBuilder {

    String UPDATE_SQL = "UPDATE %s SET %s ";

    String SAVE_SQL = "INSERT INTO %s (%s) VALUES (%s)";

    String DELETE_SQL = "DELETE %s ";

    String selectSql(Class<?> entity);

    String updateSql(Class<?> entity);

    String saveSql(Class<?> entity);

    String deleteSql(Class<?> entity);
}
