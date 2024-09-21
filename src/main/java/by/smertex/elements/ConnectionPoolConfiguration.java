package by.smertex.elements;

public record ConnectionPoolConfiguration (String connectionUrl,
                                           String connectionUsername,
                                           String password,
                                           String connectionDriverClass,
                                           Integer poolSize,
                                           Integer poolExpansion) {
}
