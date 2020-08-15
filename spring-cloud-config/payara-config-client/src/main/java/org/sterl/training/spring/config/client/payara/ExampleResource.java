package org.sterl.training.spring.config.client.payara;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.sterl.training.spring.config.client.payara.configsource.SpringConfigServiceConfigSource;

@Path("/")
@ApplicationScoped
public class ExampleResource {

    @Inject Config config;
    
    // https://github.com/eclipse/microprofile-config/blob/master/spec/src/main/asciidoc/configsources.asciidoc#custom-configsources-via-configsourceprovider
    // PAYARA Micro doesn't implement  dynamic configuration correctly, means it is bugged!
    // it will reload but somehow random ...
    @Inject
    @ConfigProperty(name = "config.foo")
    Provider<String> foo;
    
    @GET
    public Response get() {
        JsonObjectBuilder result = Json.createObjectBuilder();
        result.add("Provider", foo.get());
        result.add("Config.getValue", config.getValue("config.foo", String.class));

        return Response.status(Status.OK)
                .entity(result.build())
                .build();
    }
    
    @POST
    @Path("/refresh")
    public void reload() {
        for(ConfigSource cs : config.getConfigSources()) {
            if (cs instanceof SpringConfigServiceConfigSource) {
                ((SpringConfigServiceConfigSource)cs).reload();
            }
        }
    }
}
