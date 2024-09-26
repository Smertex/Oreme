package by.smertex.interfaces.session;

public interface Transaction {
    void begin();

    void sqlInput(String sql);

    void rollback();

    void commit();
}
