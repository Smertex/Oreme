package by.smertex.realisation.session;

import by.smertex.interfaces.cfg.ConnectionManager;
import by.smertex.interfaces.cfg.InitializationManager;
import by.smertex.interfaces.session.Session;
import by.smertex.interfaces.session.SessionFactory;
import by.smertex.realisation.cfg.ConnectionManagerBasicRealisation;
import by.smertex.realisation.cfg.InitializationManagerBasicRealisation;

public class SessionFactoryBasicRealisation implements SessionFactory {

    private final ConnectionManager connectionManager = ConnectionManagerBasicRealisation.getInstance();

    private final InitializationManager initializationManager = InitializationManagerBasicRealisation.getInstance();

    @Override
    public Session getSession() {
        return new SessionBasicRealisation(connectionManager.getConnection(),
                initializationManager.getConfiguration().isolationLevel());
    }
}
