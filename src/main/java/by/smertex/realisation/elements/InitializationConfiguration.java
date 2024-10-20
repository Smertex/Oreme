package by.smertex.realisation.elements;

public record InitializationConfiguration(InitializationStrategy initializationStrategy,
                                          IsolationLevel isolationLevel,
                                          Boolean sessionCache) {

    public static final class BuilderInitializationConfiguration{
        private InitializationStrategy initializationStrategy;

        private IsolationLevel isolationLevel;

        private Boolean sessionCache;

        public BuilderInitializationConfiguration setInitializationStrategy(InitializationStrategy initializationStrategy) {
            this.initializationStrategy = initializationStrategy;
            return this;
        }

        public BuilderInitializationConfiguration setIsolationLevel(String isolationLevel) {
            this.isolationLevel = isolationLevel == null ?
                    IsolationLevel.SERIALIZABLE : IsolationLevel.valueOf(isolationLevel.toUpperCase());
            return this;
        }

        public BuilderInitializationConfiguration setSessionCache(String sessionCache) {
            this.sessionCache = Boolean.parseBoolean(sessionCache);
            return this;
        }

        public InitializationConfiguration build(){
            return new InitializationConfiguration(initializationStrategy, isolationLevel, sessionCache);
        }
    }
}
