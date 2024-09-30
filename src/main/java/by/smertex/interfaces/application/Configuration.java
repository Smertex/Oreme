package by.smertex.interfaces.application;

import by.smertex.interfaces.application.session.SessionFactory;

public interface Configuration {
    SessionFactory createSessionFactory();
}
