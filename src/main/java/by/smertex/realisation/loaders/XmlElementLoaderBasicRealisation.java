package by.smertex.realisation.loaders;

import by.smertex.interfaces.loaders.XmlElementLoader;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class XmlElementLoaderBasicRealisation implements XmlElementLoader {
    private static final XmlElementLoaderBasicRealisation INSTANCE = new XmlElementLoaderBasicRealisation();

    private final Map<String, Node> xmlElements = new HashMap<>();

    private void init(){
        Node node = XmlElementLoader.findRootInXml(XmlElementLoader.initializationDocument()
                .getChildNodes());
        feelXmlElements(node.getChildNodes());
    }

    private void feelXmlElements(NodeList nodes){
        IntStream.range(0, nodes.getLength())
                .mapToObj(nodes::item)
                .forEach(element -> xmlElements.put(element.getNodeName(), element));
    }

    @Override
    public Node getNodeByTag(String key) {
        return xmlElements.get(key);
    }

    public static XmlElementLoaderBasicRealisation getInstance(){
        return INSTANCE;
    }

    private XmlElementLoaderBasicRealisation(){
        init();
    }
}
