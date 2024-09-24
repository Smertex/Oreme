package by.smertex.realisation.elements;

public record InitializationConfiguration(InitializationStrategy initializationStrategy,
                                          Boolean queryGenerate) {

    public static final class BuilderInitializationConfiguration{
        private InitializationStrategy initializationStrategy;
        private Boolean queryGenerate;

        public BuilderInitializationConfiguration setInitializationStrategy(InitializationStrategy initializationStrategy) {
            this.initializationStrategy = initializationStrategy;
            return this;
        }

        public BuilderInitializationConfiguration setQueryGenerate(Boolean queryGenerate) {
            this.queryGenerate = queryGenerate;
            return this;
        }

        public InitializationConfiguration build(){
            return new InitializationConfiguration(initializationStrategy, queryGenerate);
        }
    }
}
