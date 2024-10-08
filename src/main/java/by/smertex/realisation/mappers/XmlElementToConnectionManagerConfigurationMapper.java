package by.smertex.realisation.mappers;


import by.smertex.realisation.elements.ConnectionManagerConfiguration;
import by.smertex.exceptions.mapper.MapFromException;
import by.smertex.interfaces.mapper.Mapper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class XmlElementToConnectionManagerConfigurationMapper implements Mapper<ConnectionManagerConfiguration, Node> {
    private static XmlElementToConnectionManagerConfigurationMapper INSTANCE = new XmlElementToConnectionManagerConfigurationMapper();

    @Override
    public ConnectionManagerConfiguration mapFrom(Node node) {
        Map<String, String> property = nodeToMap(node.getChildNodes());
        try {
            return new ConnectionManagerConfiguration.BuilderConnectionManagerConfiguration()
                    .setConnectionUrl(property.get("connection.url"))
                    .setConnectionUsername(property.get("connection.username"))
                    .setPassword(property.get("connection.password"))
                    .setConnectionDriverClass(property.get("connection.driver.class"))
                    .setPoolSize(property.get("pool.size"))
                    .setPoolExpansion(property.get("pool.expansion"))
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

    public static XmlElementToConnectionManagerConfigurationMapper getInstance() {
        return INSTANCE;
    }

    private XmlElementToConnectionManagerConfigurationMapper(){

    }
}
