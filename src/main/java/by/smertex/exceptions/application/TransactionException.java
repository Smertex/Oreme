package by.smertex.exceptions.application;

public class TransactionException extends RuntimeException{
    public TransactionException(Throwable e){
        super(e);
    }
}
