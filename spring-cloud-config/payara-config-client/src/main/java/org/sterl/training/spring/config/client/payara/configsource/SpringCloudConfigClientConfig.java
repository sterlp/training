package org.sterl.training.spring.config.client.payara.configsource;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;

public class SpringCloudConfigClientConfig {


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
    
    public String buildBasicAuthz() throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString((username.get() + ":" + password.get()).getBytes("UTF-8"));
    }
}
