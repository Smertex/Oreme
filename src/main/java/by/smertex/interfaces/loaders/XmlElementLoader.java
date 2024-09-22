package by.smertex.interfaces.loaders;

import by.smertex.exceptions.InitXmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.stream.IntStream;

public interface XmlElementLoader {

    String XML_FILE_NAME = "oreme.configuration.xml";

    String XML_ROOT_TAG = "oreme-configuration";

    Node getNodeByTag(String key);

    static Document initializationDocument(){
        try(var stream = ClassLoader.getSystemClassLoader().getResourceAsStream(XML_FILE_NAME)) {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(stream);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new InitXmlException(e);
        }
    }

    static Node findRootInXml(NodeList elements){
        return IntStream.range(0, elements.getLength())
                .mapToObj(elements::item)
                .filter(node -> node.getNodeName().equals(XML_ROOT_TAG))
                .findFirst()
                .orElseThrow(() -> new InitXmlException(new RuntimeException()));
    }
}
