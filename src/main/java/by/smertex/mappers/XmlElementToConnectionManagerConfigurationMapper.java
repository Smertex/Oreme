package by.smertex.mappers;


import by.smertex.elements.ConnectionManagerConfiguration;
import by.smertex.interfaces.mapper.Mapper;
import org.w3c.dom.Element;

public class XmlElementToConnectionManagerConfigurationMapper implements Mapper<ConnectionManagerConfiguration, Element> {
    private static XmlElementToConnectionManagerConfigurationMapper INSTANCE = new XmlElementToConnectionManagerConfigurationMapper();

    @Override
    public ConnectionManagerConfiguration mapFrom(Element element) {
        return null;
    }

    public static XmlElementToConnectionManagerConfigurationMapper getInstance() {
        return INSTANCE;
    }

    private XmlElementToConnectionManagerConfigurationMapper(){

    }
}
