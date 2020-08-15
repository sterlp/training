package org.sterl.training.spring.config.client.payara.configsource;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Response {
    private final String name;
    private final List<String> profiles;
    private final List<PropertySource> propertySources;

    @JsonCreator
    public Response(@JsonProperty("name") String name, @JsonProperty("profiles") List<String> profiles,
            @JsonProperty("propertySources") List<PropertySource> propertySources) {
        this.name = name;
        this.profiles = profiles;
        this.propertySources = propertySources;
    }

    @Data
    public static class PropertySource {
        private final String name;
        private final Map<String, String> source;

        @JsonCreator
        public PropertySource(@JsonProperty("name") String name, @JsonProperty("source") Map<String, String> source) {
            this.name = name;
            this.source = source;
        }
    }
}
