package by.smertex.interfaces.application.session;

public interface Transaction {
    void begin();

    void rollback();

    void commit();
}
