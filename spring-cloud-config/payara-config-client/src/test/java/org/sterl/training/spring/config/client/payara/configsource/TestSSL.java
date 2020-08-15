package org.sterl.training.spring.config.client.payara.configsource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

class TestSSL {

    @Test
    void test() {
        System.out.println(DefaultSpringCloudConfigClientGateway.class.getResource("/trust-store.jks"));
        //System.setProperty("javax.net.ssl.trustStore", DefaultSpringCloudConfigClientGateway.class.getResource("/trust-store.jks").getFile()); 
        //System.setProperty("javax.net.ssl.trustStoreType", "jks");

        SpringConfigServiceConfigSource configSource = new SpringConfigServiceConfigSource();
        
        for (Entry<String, String> e : configSource.getProperties().entrySet()) {
            System.out.println(e.getKey() + " = " + e.getValue());
        }
        
        configSource.reload();
    }

}
