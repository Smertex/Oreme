package by.smertex.realisation.elements;

public record ConnectionManagerConfiguration(String connectionUrl,
                                             String connectionUsername,
                                             String password,
                                             String connectionDriverClass,
                                             Integer poolSize,
                                             Integer poolExpansion) {

    private static final Integer DEFAULT_POOL_SIZE = 10;

    private static final Integer DEFAULT_EXTENSION_POOL_SIZE = 10;

    public static class BuilderConnectionManagerConfiguration{
        private String connectionUrl;
        private String connectionUsername;
        private String password;
        private String connectionDriverClass;
        private Integer poolSize;
        private Integer poolExpansion;

        public BuilderConnectionManagerConfiguration setConnectionUrl(String connectionUrl) {
            this.connectionUrl = connectionUrl;
            return this;
        }

        public BuilderConnectionManagerConfiguration setConnectionUsername(String connectionUsername) {
            this.connectionUsername = connectionUsername;
            return this;
        }

        public BuilderConnectionManagerConfiguration setPassword(String password) {
            this.password = password;
            return this;
        }

        public BuilderConnectionManagerConfiguration setConnectionDriverClass(String connectionDriverClass) {
            this.connectionDriverClass = connectionDriverClass;
            return this;
        }

        public BuilderConnectionManagerConfiguration setPoolSize(String poolSize) {
            this.poolSize = poolSize != null ?
                    Integer.parseInt(poolSize) : DEFAULT_POOL_SIZE;
            return this;
        }

        public BuilderConnectionManagerConfiguration setPoolExpansion(String poolExpansion) {
            this.poolExpansion = poolExpansion != null ?
                    Integer.parseInt(poolExpansion) : DEFAULT_EXTENSION_POOL_SIZE;
            return this;
        }

        public ConnectionManagerConfiguration build(){
            return new ConnectionManagerConfiguration(
                    connectionUrl,
                    connectionUsername,
                    password,
                    connectionDriverClass,
                    poolSize,
                    poolExpansion
            );
        }
    }
}
