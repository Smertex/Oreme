package by.smertex.interfaces.session;

public interface Transaction {
    void begin();

    void rollback();

    void commit();
}
