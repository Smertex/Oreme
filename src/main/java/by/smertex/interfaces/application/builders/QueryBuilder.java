package by.smertex.interfaces.application.builders;

import by.smertex.interfaces.application.session.CompositeKey;

public interface QueryBuilder {
    String SELECT_SQL = "SELECT %s \n";

    String FROM_SQL = "FROM %s \n";

    String WHERE_SQL = "WHERE %s ";

    String AND_SQL = " AND ";

}
