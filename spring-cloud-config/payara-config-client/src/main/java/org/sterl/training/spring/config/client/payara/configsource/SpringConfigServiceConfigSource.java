package org.sterl.training.spring.config.client.payara.configsource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.sterl.training.spring.config.client.payara.configsource.Response.PropertySource;


public class SpringConfigServiceConfigSource implements ConfigSource {
    private final DefaultSpringCloudConfigClientGateway springGateway;
    
    private volatile Map<String, String> config = new LinkedHashMap<>();

    public SpringConfigServiceConfigSource() {
        System.out.println(DefaultSpringCloudConfigClientGateway.class.getResource("/trust-store.jks"));
        System.setProperty("javax.net.ssl.trustStore", DefaultSpringCloudConfigClientGateway.class.getResource("/trust-store.jks").getFile()); 
        System.setProperty("javax.net.ssl.trustStoreType", "jks");
        
        SpringCloudConfigClientConfig config = new SpringCloudConfigClientConfig();
        springGateway = new DefaultSpringCloudConfigClientGateway(config);
        reload();
    }
    
    public void reload() {
        try {
            Map<String, String> newConfig = new LinkedHashMap<>();
            
            Response exchange = springGateway.exchange("service1", "dev");
            List<PropertySource> propertySources = exchange.getPropertySources();
            for (int i = propertySources.size(); i > 0; --i) {
                PropertySource propertySource = propertySources.get(i);
                
                newConfig.putAll(propertySource.getSource());
            }
            config = newConfig;
            System.out.println("Loaded: " + config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getOrdinal() {
        return 450;
    }

    @Override
    public Map<String, String> getProperties() {
        return config;
    }

    @Override
    public String getValue(String propertyName) {
        return config.get(propertyName);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
