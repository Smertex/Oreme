package by.smertex.exceptions.application;

import by.smertex.realisation.application.session.LazyInitializerBasicRealisation;

public class LazyInitializationException extends RuntimeException{
    public LazyInitializationException(Throwable e){
        super(e);
    }
}
