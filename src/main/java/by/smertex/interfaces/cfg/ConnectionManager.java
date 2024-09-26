package by.smertex.interfaces.cfg;

import by.smertex.exceptions.cfg.LoadDriverException;
import by.smertex.exceptions.cfg.OpenConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface ConnectionManager {

    String XML_CONNECTION_MANAGER_CONFIGURATION_TAG = "connection-manager-configuration";

    Connection getConnection();

    default void loadDriver(String driverClass){
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new LoadDriverException(e);
        }
    }

    default Connection openConnection(String connectionUrl, String connectionUsername, String password){
        try {
            return DriverManager.getConnection(connectionUrl, connectionUsername, password);
        } catch (SQLException e) {
            throw new OpenConnectionException(e);
        }
    }
}
