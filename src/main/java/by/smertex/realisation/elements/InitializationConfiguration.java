package by.smertex.realisation.elements;

public record InitializationConfiguration(InitializationStrategy initializationStrategy,
                                          IsolationLevel isolationLevel,
                                          Boolean queryGenerate) {

    public static final class BuilderInitializationConfiguration{
        private InitializationStrategy initializationStrategy;

        private IsolationLevel isolationLevel;

        private Boolean queryGenerate;

        public BuilderInitializationConfiguration setInitializationStrategy(InitializationStrategy initializationStrategy) {
            this.initializationStrategy = initializationStrategy;
            return this;
        }

        public BuilderInitializationConfiguration setIsolationLevel(String isolationLevel) {
            this.isolationLevel = isolationLevel == null ?
                    IsolationLevel.SERIALIZABLE : IsolationLevel.valueOf(isolationLevel.toUpperCase());
            return this;
        }

        public BuilderInitializationConfiguration setQueryGenerate(String queryGenerate) {
            this.queryGenerate = Boolean.parseBoolean(queryGenerate);
            return this;
        }

        public InitializationConfiguration build(){
            return new InitializationConfiguration(initializationStrategy, isolationLevel, queryGenerate);
        }
    }
}
