package by.smertex.interfaces.application;

public interface QueryBuilder {
    String SELECT_SQL = "SELECT %s ";

    String FROM_SQL = "FROM %s ";

    String WHERE_SQL = "WHERE %s ";

    String JOIN_SQL = "JOIN %s ON %s ";

    String UPDATE_SQL = "UPDATE %s SET %s ";

    String SAVE_SQL = "INSERT INTO %s (%s) VALUES (%s)";

    String DELETE_SQL = "DELETE %s ";
}
