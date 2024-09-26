package by.smertex.interfaces.session;

public interface Transaction {
    void begin();

    Object sqlInput(String sql);

    void rollback();

    void commit();
}
