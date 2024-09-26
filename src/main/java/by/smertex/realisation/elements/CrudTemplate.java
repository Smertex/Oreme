package by.smertex.realisation.elements;

public final class CrudTemplate {

    private final String SELECT = "SELECT %s FROM %s.%s";

    private final String WHERE = "WHERE %s";

    private final String JOIN = "JOIN %s ON %s";


    public String getSelect() {
        return SELECT;
    }

    public String getWhere() {
        return WHERE;
    }

    public String getJoin() {
        return JOIN;
    }
}
