package by.smertex.elements;

public record ConnectionManagerConfiguration(String connectionUrl,
                                             String connectionUsername,
                                             String password,
                                             String connectionDriverClass,
                                             Integer poolSize,
                                             Integer poolExpansion) {
}
