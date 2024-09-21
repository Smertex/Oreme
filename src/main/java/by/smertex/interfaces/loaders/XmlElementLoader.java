package by.smertex.interfaces.loaders;

import by.smertex.exceptions.InitXmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.IntStream;

public interface XmlElementLoader {

    String XML_FILE_NAME = "oreme.configuration.xml";

    String XML_ROOT_TAG = "oreme-configuration";

    String XML_CONNECTION_MANAGER_CONFIGURATION_TAG = "connection-manager-configuration";

    String XML_INITIALIZATION_CONFIGURATION_TAG = "initialization-configuration";

    String XML_ENTITIES_TAG = "entities";

    Element getElementByTag(String key);

    static Document initializationDocument(){
        try(var stream = ClassLoader.getSystemClassLoader().getResourceAsStream(XML_FILE_NAME)) {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(stream);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new InitXmlException(e);
        }
    }

    static Element findRootInXml(Document document){
        NodeList elements = document.getDocumentElement().getChildNodes();
        Optional<Element> rootElement = IntStream.range(0, elements.getLength())
                .mapToObj(elements::item)
                .filter(node -> node.getNodeName().equals(XML_ROOT_TAG))
                .map(node -> (Element) node)
                .findFirst();

        if(rootElement.isEmpty()) throw new InitXmlException(new RuntimeException());
        return rootElement.get();
    }
}
