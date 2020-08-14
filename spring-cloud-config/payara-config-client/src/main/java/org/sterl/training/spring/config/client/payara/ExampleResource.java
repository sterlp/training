package org.sterl.training.spring.config.client.payara;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/")
@ApplicationScoped
public class ExampleResource {

    @GET
    public Response get() {
        JsonObjectBuilder result = Json.createObjectBuilder();
        result.add("key", "hallo");

        return Response.status(Status.OK)
                .entity(result.build())
                .build();
    }
}
