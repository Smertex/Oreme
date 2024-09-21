package by.smertex.connections;

import by.smertex.interfaces.connections.ConnectionManager;

import java.sql.Connection;

public class ConnectionManagerBasicRealisation implements ConnectionManager {
    @Override
    public Connection getConnection() {
        return null;
    }
}
