package by.smertex.realisation.mappers;

import by.smertex.interfaces.mapper.Mapper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;
import java.util.stream.IntStream;

public class XmlElementToListEntityClassesMapper implements Mapper<List<String>, Node> {

    private static final XmlElementToListEntityClassesMapper INSTANCE = new XmlElementToListEntityClassesMapper();

    @Override
    public List<String> mapFrom(Node node) {
        NodeList xmlEntitiesClasses = node.getChildNodes();
        return IntStream.range(0, xmlEntitiesClasses.getLength())
                .mapToObj(xmlEntitiesClasses::item)
                .filter(item -> item.getNodeName().equals("mapping"))
                .map(item -> item.getAttributes().getNamedItem("class").getNodeValue())
                .toList();

    }

    public static XmlElementToListEntityClassesMapper getInstance(){
        return INSTANCE;
    }

    private XmlElementToListEntityClassesMapper(){

    }
}
