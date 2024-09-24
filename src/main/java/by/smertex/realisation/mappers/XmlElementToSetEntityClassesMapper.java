package by.smertex.realisation.mappers;

import by.smertex.interfaces.mapper.Mapper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class XmlElementToSetEntityClassesMapper implements Mapper<Set<String>, Node> {

    private static final XmlElementToSetEntityClassesMapper INSTANCE = new XmlElementToSetEntityClassesMapper();

    @Override
    public Set<String> mapFrom(Node node) {
        NodeList xmlEntitiesClasses = node.getChildNodes();
        return IntStream.range(0, xmlEntitiesClasses.getLength())
                .mapToObj(xmlEntitiesClasses::item)
                .filter(item -> item.getNodeName().equals("mapping"))
                .map(item -> item.getAttributes().getNamedItem("class").getNodeValue())
                .collect(Collectors.toSet());

    }

    public static XmlElementToSetEntityClassesMapper getInstance(){
        return INSTANCE;
    }

    private XmlElementToSetEntityClassesMapper(){

    }
}
