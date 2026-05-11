package com.framework.config;

public final class ConfigManager {

    private static ConfigFactory config;

    private ConfigManager() {
    }

    public static ConfigFactory getConfig() {
        if (config == null) {
            synchronized (ConfigManager.class) {
                if (config == null) {
                    config = org.aeonbits.owner.ConfigFactory.create(ConfigFactory.class);
                }
            }
        }
        return config;
    }
}
