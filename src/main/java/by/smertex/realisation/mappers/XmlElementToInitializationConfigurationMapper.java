package by.smertex.realisation.mappers;

import by.smertex.exceptions.mapper.MapFromException;
import by.smertex.realisation.elements.InitializationConfiguration;
import by.smertex.interfaces.mapper.Mapper;
import by.smertex.realisation.elements.InitializationStrategy;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class XmlElementToInitializationConfigurationMapper implements Mapper<InitializationConfiguration, Node> {
    private static final XmlElementToInitializationConfigurationMapper INSTANCE = new XmlElementToInitializationConfigurationMapper();

    @Override
    public InitializationConfiguration mapFrom(Node node) {
        Map<String, String> property = nodeToMap(node.getChildNodes());
        try {
            return new InitializationConfiguration.BuilderInitializationConfiguration()
                    .setInitializationStrategy(InitializationStrategy.valueOf(property.get("strategy").toUpperCase()))
                    .setIsolationLevel(property.get("isolation-level"))
                    .setQueryGenerate(property.get("query-generate"))
                    .build();
        } catch (RuntimeException e){
            throw new MapFromException(e);
        }
    }

    private Map<String, String> nodeToMap(NodeList xmlProperties){
        Map<String, String> propertyValues = new HashMap<>();
        IntStream.range(0, xmlProperties.getLength())
                .mapToObj(xmlProperties::item)
                .filter(item -> item.getNodeName().equals("property"))
                .forEach(item -> propertyValues.put(item.getAttributes().getNamedItem("name").getNodeValue(), item.getTextContent()));
        return propertyValues;
    }

    public static XmlElementToInitializationConfigurationMapper getInstance(){
        return INSTANCE;
    }

    private XmlElementToInitializationConfigurationMapper(){

    }
}
