package org.sterl.training.spring.config.client.payara;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/")
@ApplicationScoped
public class ExampleResource {

    @Inject
    @ConfigProperty(name = "config.foo")
    String foo;
    
    @GET
    public Response get() {
        JsonObjectBuilder result = Json.createObjectBuilder();
        result.add("key", foo);

        return Response.status(Status.OK)
                .entity(result.build())
                .build();
    }
}
