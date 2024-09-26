package by.smertex.exceptions.session;

public class TransactionException extends RuntimeException{
    public TransactionException(Throwable e){
        super(e);
    }
}
