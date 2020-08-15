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
        SpringCloudConfigClientConfig config = new SpringCloudConfigClientConfig();
        springGateway = new DefaultSpringCloudConfigClientGateway(config);
        reload();
    }
    
    public void reload() {
        System.out.println("-> reload");
        try {
            final Map<String, String> newConfig = new LinkedHashMap<>();
            final Response exchange = springGateway.exchange("service1", "dev");
            final List<PropertySource> propertySources = exchange.getPropertySources();
            for (int i = propertySources.size() - 1; i >= 0 ; i--) {
                PropertySource propertySource = propertySources.get(i);
                newConfig.putAll(propertySource.getSource());
            }
            config = newConfig;
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
        System.out.println("-> getProperties");
        return config;
    }

    @Override
    public String getValue(String propertyName) {
        System.out.println("-> getValue: " + propertyName);
        return config.get(propertyName);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
