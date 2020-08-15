package org.sterl.training.spring.config.client.payara.configsource;

import java.time.Duration;
import java.util.Optional;

public class SpringCloudConfigClientConfig {

    protected static final String NAME = "spring-cloud-config";

    /**
     * If enabled, will try to read the configuration from a Spring Cloud Config Server
     */
    public boolean enabled = true;

    /**
     * If set to true, the application will not stand up if it cannot obtain configuration from the Config Server
     */
    public boolean failFast = true;

    /**
     * The Base URI where the Spring Cloud Config Server is available
     */
    public String url = "https://localhost:8000";

    /**
     * The amount of time to wait when initially establishing a connection before giving up and timing out.
     * <p>
     * Specify `0` to wait indefinitely.
     */
    public Duration connectionTimeout = Duration.ofSeconds(5);

    /**
     * The amount of time to wait for a read on a socket before an exception is thrown.
     * <p>
     * Specify `0` to wait indefinitely.
     */
    public Duration readTimeout = Duration.ofSeconds(10);

    /**
     * The username to be used if the Config Server has BASIC Auth enabled
     */
    public Optional<String> username = Optional.of("configuser");

    /**
     * The password to be used if the Config Server has BASIC Auth enabled
     */
    public Optional<String> password = Optional.of("configpass");

    public boolean usernameAndPasswordSet() {
        return username.isPresent() && password.isPresent();
    }
}
