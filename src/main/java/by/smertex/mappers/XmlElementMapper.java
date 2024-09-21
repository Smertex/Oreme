package by.smertex.mappers;


import by.smertex.elements.ConnectionPoolConfiguration;
import by.smertex.interfaces.mapper.Mapper;
import org.w3c.dom.Element;

public class XmlElementMapper implements Mapper<Element, ConnectionPoolConfiguration> {
    private static XmlElementMapper INSTANCE = new XmlElementMapper();

    @Override
    public Element mapFrom(ConnectionPoolConfiguration connectionPoolConfiguration) {
        return null;
    }

    public static XmlElementMapper getInstance() {
        return INSTANCE;
    }

    private XmlElementMapper(){

    }
}
