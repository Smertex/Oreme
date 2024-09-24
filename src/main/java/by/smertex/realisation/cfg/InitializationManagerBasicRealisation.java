package by.smertex.realisation.cfg;

import by.smertex.interfaces.cfg.InitializationManager;
import by.smertex.interfaces.loaders.XmlElementLoader;
import by.smertex.interfaces.mapper.Mapper;
import by.smertex.realisation.elements.InitializationConfiguration;
import by.smertex.realisation.loaders.XmlElementLoaderBasicRealisation;
import by.smertex.realisation.mappers.XmlElementToInitializationConfigurationMapper;
import org.w3c.dom.Node;

public class InitializationManagerBasicRealisation implements InitializationManager {

    private static final InitializationManagerBasicRealisation INSTANCE = new InitializationManagerBasicRealisation();

    private final XmlElementLoader xmlElementLoader = XmlElementLoaderBasicRealisation.getInstance();

    private final Mapper<InitializationConfiguration, Node> mapper = XmlElementToInitializationConfigurationMapper.getInstance();

    private InitializationConfiguration initializationConfiguration;

    private void initInitializationConfiguration(){
        initializationConfiguration = mapper.mapFrom(xmlElementLoader.getNodeByTag(InitializationManager.XML_INITIALIZATION_CONFIGURATION_TAG));
    }

    @Override
    public InitializationConfiguration getConfiguration() {
        return initializationConfiguration;
    }

    public static InitializationManagerBasicRealisation getInstance(){
        return INSTANCE;
    }

    private InitializationManagerBasicRealisation(){
        initInitializationConfiguration();
    }
}
