package by.smertex.exceptions.application;

public class ConnectionCloseException extends RuntimeException{
    public ConnectionCloseException(Throwable e){
        super(e);
    }
}
