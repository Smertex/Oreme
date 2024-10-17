package by.smertex.exceptions.application;

public class LazyInitializationException extends RuntimeException{
    public LazyInitializationException(Throwable e){
        super(e);
    }
}
