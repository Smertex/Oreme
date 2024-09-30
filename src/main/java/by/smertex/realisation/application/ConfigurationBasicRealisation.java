package by.smertex.realisation.application;

import by.smertex.interfaces.application.Configuration;
import by.smertex.interfaces.application.session.SessionFactory;
import by.smertex.interfaces.cfg.ConnectionManager;
import by.smertex.interfaces.cfg.EntityManager;
import by.smertex.interfaces.cfg.InitializationManager;
import by.smertex.realisation.application.session.SessionFactoryBasicRealisation;
import by.smertex.realisation.cfg.ConnectionManagerBasicRealisation;
import by.smertex.realisation.cfg.EntityManagerBasicRealisation;
import by.smertex.realisation.cfg.InitializationManagerBasicRealisation;

public class ConfigurationBasicRealisation implements Configuration{

    private final ConnectionManager connectionManager;

    private final InitializationManager initializationManager;

    private final EntityManager entityManager;


    public ConfigurationBasicRealisation(){
        this.connectionManager = ConnectionManagerBasicRealisation.getInstance();
        this.initializationManager = InitializationManagerBasicRealisation.getInstance();
        this.entityManager = EntityManagerBasicRealisation.getInstance();
        initializationDataBase();
    }

    //todo
    private void initializationDataBase(){

    }

    @Override
    public SessionFactory createSessionFactory() {
        return new SessionFactoryBasicRealisation(connectionManager,
                entityManager,
                initializationManager.getConfiguration().isolationLevel());
    }
}
