package by.smertex.realisation.application.builders;

import by.smertex.interfaces.application.builders.InstanceBuilder;
import by.smertex.interfaces.application.session.Session;

import java.lang.reflect.Method;

public class InstanceBuilderBasicRealisation implements InstanceBuilder {

    private Session session;

    private Method methodForBuildInstance;


    @Override
    public Object buildInstance() {
        return null;
    }

    public InstanceBuilderBasicRealisation(Method methodForBuildInstance) {
        this.methodForBuildInstance = methodForBuildInstance;
    }
}
