package by.smertex.interfaces.cfg;

import by.smertex.realisation.elements.InitializationConfiguration;

public interface InitializationManager{

    String XML_INITIALIZATION_CONFIGURATION_TAG = "initialization-configuration";

    InitializationConfiguration getConfiguration();
}
