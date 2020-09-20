package org.sterl.traning.ee.trx;

import java.time.Clock;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("")
@ApplicationScoped
public class TimeoutResource {

    @Inject DataAccessService service;
    @GET
    public String get() {

        return service.generateData() + " " + Clock.systemUTC().instant().toString();
    }
}
