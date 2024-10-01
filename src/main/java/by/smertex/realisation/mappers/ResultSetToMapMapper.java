package by.smertex.realisation.mappers;

import by.smertex.exceptions.mapper.MapFromException;
import by.smertex.interfaces.mapper.Mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ResultSetToMapMapper implements Mapper<Map<String, Object>, ResultSet> {

    private static final ResultSetToMapMapper INSTANCE = new ResultSetToMapMapper();

    @Override
    public Map<String, Object> mapFrom(ResultSet resultSet) {
        Map<String, Object> map = new HashMap<>();
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++)
                map.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
        } catch (SQLException e) {
            throw new MapFromException(e);
        }
        return map;
    }

    public static ResultSetToMapMapper getInstance() {
        return INSTANCE;
    }
}
