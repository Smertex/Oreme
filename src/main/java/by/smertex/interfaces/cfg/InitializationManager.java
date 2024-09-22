package by.smertex.interfaces.cfg;

import by.smertex.elements.ConnectionManagerConfiguration;

public interface InitializationManager{

    String XML_INITIALIZATION_CONFIGURATION_TAG = "initialization-configuration";

    ConnectionManagerConfiguration getConfiguration();
}
