package by.smertex.exceptions.mapper;

import by.smertex.realisation.mappers.ResultSetToObjectMapperBasicRealisation;

public class ResultSetToObjectMapperException extends RuntimeException{
    public ResultSetToObjectMapperException(Throwable e){
        super(e);
    }
}
