package by.smertex.realisation.cfg;

import by.smertex.interfaces.cfg.InitializationManager;
import by.smertex.interfaces.loaders.XmlElementLoader;
import by.smertex.interfaces.mapper.Mapper;
import by.smertex.realisation.elements.InitializationConfiguration;
import org.w3c.dom.Node;

public class InitializationManagerBasicRealisation implements InitializationManager {

    private final XmlElementLoader xmlElementLoader;

    private final Mapper<InitializationConfiguration, Node> mapper;

    private final InitializationConfiguration initializationConfiguration;

    @Override
    public InitializationConfiguration getConfiguration() {
        return initializationConfiguration;
    }

    private InitializationConfiguration initInitializationConfiguration(){
        return mapper.mapFrom(xmlElementLoader.getNodeByTag(InitializationManager.XML_INITIALIZATION_CONFIGURATION_TAG));
    }

    protected InitializationManagerBasicRealisation(XmlElementLoader xmlElementLoader,
                                                 Mapper<InitializationConfiguration, Node> mapper){
        this.xmlElementLoader = xmlElementLoader;
        this.mapper = mapper;
        this.initializationConfiguration = initInitializationConfiguration();
    }
}
