package by.smertex.interfaces.cfg;

import by.smertex.interfaces.application.session.SessionFactory;

public interface Configuration {
    SessionFactory createSessionFactory();
}
