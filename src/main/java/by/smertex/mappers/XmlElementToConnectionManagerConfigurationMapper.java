package by.smertex.mappers;


import by.smertex.elements.ConnectionManagerConfiguration;
import by.smertex.interfaces.mapper.Mapper;
import org.w3c.dom.Node;

public class XmlElementToConnectionManagerConfigurationMapper implements Mapper<ConnectionManagerConfiguration, Node> {
    private static XmlElementToConnectionManagerConfigurationMapper INSTANCE = new XmlElementToConnectionManagerConfigurationMapper();

    @Override
    public ConnectionManagerConfiguration mapFrom(Node node) {
        return null;
    }

    public static XmlElementToConnectionManagerConfigurationMapper getInstance() {
        return INSTANCE;
    }

    private XmlElementToConnectionManagerConfigurationMapper(){

    }
}
