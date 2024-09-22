package by.smertex.mappers;

import by.smertex.elements.InitializationConfiguration;
import by.smertex.interfaces.mapper.Mapper;
import org.w3c.dom.Node;

public class XmlElementToInitializationConfigurationMapper implements Mapper<InitializationConfiguration, Node> {
    private final XmlElementToInitializationConfigurationMapper INSTANCE = new XmlElementToInitializationConfigurationMapper();

    @Override
    public InitializationConfiguration mapFrom(Node node) {
        return null;
    }

    public XmlElementToInitializationConfigurationMapper getInstance(){
        return INSTANCE;
    }

    private XmlElementToInitializationConfigurationMapper(){

    }
}
