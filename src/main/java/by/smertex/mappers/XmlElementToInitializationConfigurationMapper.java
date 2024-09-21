package by.smertex.mappers;

import by.smertex.elements.InitializationConfiguration;
import by.smertex.interfaces.mapper.Mapper;
import org.w3c.dom.Element;

public class XmlElementToInitializationConfigurationMapper implements Mapper<InitializationConfiguration, Element> {
    private final XmlElementToInitializationConfigurationMapper INSTANCE = new XmlElementToInitializationConfigurationMapper();

    @Override
    public InitializationConfiguration mapFrom(Element element) {
        return null;
    }

    public XmlElementToInitializationConfigurationMapper getInstance(){
        return INSTANCE;
    }

    private XmlElementToInitializationConfigurationMapper(){

    }
}
