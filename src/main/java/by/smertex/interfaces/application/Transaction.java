package by.smertex.interfaces.application;

public interface Transaction {
    void begin();

    void rollback();

    void commit();
}
