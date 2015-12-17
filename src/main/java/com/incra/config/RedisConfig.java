package com.incra.config;

/**
 * Redis configuration
 *
 * @author Jeff Risberg
 * @since 12/04/15
 */
public class RedisConfig {
    private final AppConfig appConfig;
    private final String prefix;

    public RedisConfig(final AppConfig appConfig, final String prefix) {
        this.appConfig = appConfig;
        this.prefix = prefix;
    }

    public String getServer() {
        return appConfig.getString(prefix + ".server", null);
    }

    public Integer getPort() {
        return appConfig.getInt(prefix + ".port", 0);
    }

    public String getPassword() {
        return appConfig.getString(prefix + ".password", null);
    }
}
