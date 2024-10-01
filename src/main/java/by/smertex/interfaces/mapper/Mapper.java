package by.smertex.interfaces.mapper;

import java.sql.SQLException;

public interface Mapper <T, F>{
    T mapFrom(F f);
}
