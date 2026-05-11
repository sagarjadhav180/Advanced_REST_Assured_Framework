package com.framework.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources({
    "system:properties",
    "system:env",
    "classpath:config.properties"
})
public interface ConfigFactory extends Config {

    @Key("base.url")
    @DefaultValue("https://reqres.in")
    String baseUrl();

    @Key("base.path")
    @DefaultValue("/api")
    String basePath();

    @Key("connection.timeout")
    @DefaultValue("10000")
    int connectionTimeout();

    @Key("socket.timeout")
    @DefaultValue("10000")
    int socketTimeout();

    @Key("auth.token")
    String authToken();

    @Key("environment")
    @DefaultValue("qa")
    String environment();

    @Key("log.request")
    @DefaultValue("true")
    boolean logRequest();

    @Key("log.response")
    @DefaultValue("true")
    boolean logResponse();

    @Key("schema.validation.enabled")
    @DefaultValue("true")
    boolean schemaValidationEnabled();

    @Key("retry.count")
    @DefaultValue("3")
    int retryCount();

    @Key("retry.delay.ms")
    @DefaultValue("1000")
    int retryDelayMs();
}
