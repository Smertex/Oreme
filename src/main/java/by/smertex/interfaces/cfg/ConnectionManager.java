package by.smertex.interfaces.cfg;

import java.sql.Connection;

public interface ConnectionManager {

    String XML_CONNECTION_MANAGER_CONFIGURATION_TAG = "connection-manager-configuration";

    Connection getConnection();
}
