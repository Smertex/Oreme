package by.smertex.realisation.elements;

public enum IsolationLevel {
    SERIALIZABLE(java.sql.Connection.TRANSACTION_SERIALIZABLE),
    REPEATABLE_READ(java.sql.Connection.TRANSACTION_REPEATABLE_READ),
    READ_COMMITTED(java.sql.Connection.TRANSACTION_READ_COMMITTED),
    READ_UNCOMMITTED(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

    private final Integer level;

    IsolationLevel(Integer level){
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }
}
