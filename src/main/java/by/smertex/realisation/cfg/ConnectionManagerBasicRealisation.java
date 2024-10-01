package by.smertex.realisation.cfg;

import by.smertex.realisation.elements.ConnectionManagerConfiguration;
import by.smertex.exceptions.cfg.TakeConnectionException;
import by.smertex.interfaces.cfg.ConnectionManager;
import by.smertex.interfaces.loaders.XmlElementLoader;
import by.smertex.interfaces.mapper.Mapper;
import org.w3c.dom.Node;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionManagerBasicRealisation implements ConnectionManager {

    private final XmlElementLoader xmlElementLoader;

    private final Mapper<ConnectionManagerConfiguration, Node> mapper;

    private final BlockingQueue<Connection> connectionPool;

    private ConnectionManagerConfiguration connectionManagerConfiguration;

    private void initConfiguration(){
        Node node = xmlElementLoader.getNodeByTag(ConnectionManager.XML_CONNECTION_MANAGER_CONFIGURATION_TAG);
        connectionManagerConfiguration = mapper.mapFrom(node);
    }

    public void init(){
        loadDriver(connectionManagerConfiguration.connectionDriverClass());
        feelConnectionPool(connectionManagerConfiguration.poolSize());
    }

    private void feelConnectionPool(Integer size){
        for(int i = 0; i < size; i++){
            connectionPool.add(proxyConnection(openConnection(connectionManagerConfiguration.connectionUrl(),
                                                              connectionManagerConfiguration.connectionUsername(),
                                                              connectionManagerConfiguration.password())));
        }
    }

    private Connection proxyConnection(Connection connection){
        return (Connection) Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(),
                new Class[]{Connection.class},
                ((proxy, method, args) -> {
                    if(method.getName().equals("close") && connectionManagerConfiguration.poolSize() > connectionPool.size()){
                        synchronized (connectionPool){
                            if(connectionManagerConfiguration.poolSize() > connectionPool.size()) connectionPool.add((Connection) proxy);
                        }
                    }
                    return method.invoke(connection, args);
                }));
    }

    private void expansionPool(){
        feelConnectionPool(connectionManagerConfiguration.poolExpansion());
    }

    @Override
    public Connection getConnection() {
        try {
            if(connectionPool.isEmpty()) {
                synchronized (ConnectionManagerBasicRealisation.class){
                    if(connectionPool.isEmpty()) expansionPool();
                }
            }
            return connectionPool.take();
        } catch (InterruptedException e) {
            throw new TakeConnectionException(e);
        }
    }

    protected ConnectionManagerBasicRealisation(XmlElementLoader xmlElementLoader,
                                             Mapper<ConnectionManagerConfiguration, Node> mapper){
        this.xmlElementLoader = xmlElementLoader;
        this.mapper = mapper;
        connectionPool = new LinkedBlockingQueue<>();
        initConfiguration();
        init();
    }

}
