package by.smertex.loaders;

import by.smertex.interfaces.loaders.XmlElementLoader;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

public class XmlElementLoaderBasicRealisation implements XmlElementLoader {
    private static final XmlElementLoaderBasicRealisation INSTANCE = new XmlElementLoaderBasicRealisation();

    private final Map<String, Element> xmlElements = new HashMap<>();

    private void init(){
        Element element = XmlElementLoader.findRootInXml(XmlElementLoader.initializationDocument());
        feelXmlElements(element);
    }

    private void feelXmlElements(Element element){

    }

    @Override
    public Element getElementByTag(String key) {
        return null;
    }

    public static XmlElementLoaderBasicRealisation getInstance(){
        return INSTANCE;
    }

    private XmlElementLoaderBasicRealisation(){
        init();
    }
}
