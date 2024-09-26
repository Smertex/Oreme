package by.smertex.exceptions.session;

public class ConnectionCloseException extends RuntimeException{
    public ConnectionCloseException(Throwable e){
        super(e);
    }
}
