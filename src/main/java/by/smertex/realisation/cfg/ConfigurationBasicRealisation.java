package by.smertex.realisation.cfg;

import by.smertex.interfaces.application.builders.*;
import by.smertex.interfaces.application.session.*;
import by.smertex.interfaces.cfg.*;
import by.smertex.interfaces.loaders.*;
import by.smertex.interfaces.mapper.Mapper;
import by.smertex.realisation.application.builders.*;
import by.smertex.realisation.application.session.*;
import by.smertex.realisation.elements.*;
import by.smertex.realisation.loaders.*;
import by.smertex.realisation.mappers.*;
import org.w3c.dom.Node;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Set;

public class ConfigurationBasicRealisation implements Configuration{
    private final XmlElementLoader xmlElementLoader;
    private final ConnectionManager connectionManager;
    private final InitializationManager initializationManager;
    private final EntityManager entityManager;
    private final Mapper<Map<String, Object>, ResultSet> resultSetToMapMapper;
    private final Mapper<ConnectionManagerConfiguration, Node> xmlElementToConnectionManagerConfigurationMapper;
    private final Mapper<InitializationConfiguration, Node> xmlElementToInitializationConfigurationMapper;
    private final Mapper<Set<String>, Node> xmlElementToSetEntityClassesMapper;
    private final ProxyEntityBuilder proxyEntityBuilder;
    private final QueryBuilder queryBuilder;
    private final InstanceBuilder instanceBuilder;
    private final CacheFactory sessionCacheFactory;

    public ConfigurationBasicRealisation(){
        this.xmlElementLoader = XmlElementLoaderBasicRealisation.getInstance();

        this.resultSetToMapMapper = ResultSetToMapMapper.getInstance();
        this.xmlElementToConnectionManagerConfigurationMapper = XmlElementToConnectionManagerConfigurationMapper.getInstance();
        this.xmlElementToInitializationConfigurationMapper = XmlElementToInitializationConfigurationMapper.getInstance();
        this.xmlElementToSetEntityClassesMapper = XmlElementToSetEntityClassesMapper.getInstance();

        this.connectionManager = new ConnectionManagerBasicRealisation(xmlElementLoader, xmlElementToConnectionManagerConfigurationMapper);
        this.initializationManager = new InitializationManagerBasicRealisation(xmlElementLoader, xmlElementToInitializationConfigurationMapper);
        this.entityManager = new EntityManagerBasicRealisation(xmlElementLoader, xmlElementToSetEntityClassesMapper);

        this.proxyEntityBuilder = new ProxyEntityBuilderBasicRealisation();
        this.queryBuilder = new QueryBuilderBasicRealisation(entityManager);
        this.instanceBuilder = new InstanceBuilderBasicRealisation(entityManager, proxyEntityBuilder);
        this.sessionCacheFactory = new CacheFactoryBasicRealisation();
        initializationDataBase();
    }

    //todo
    private void initializationDataBase(){

    }

    @Override
    public SessionFactory createSessionFactory() {
        return new SessionFactoryBasicRealisation(connectionManager,
                initializationManager.getConfiguration().isolationLevel(),
                sessionCacheFactory,
                instanceBuilder,
                queryBuilder);
    }
}
