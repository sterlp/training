package org.sterl.traning.ee.trx;

import java.time.Clock;
import java.time.Duration;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.sterl.traning.ee.trx.timeout.TransactionTimeout;

@Path("")
@ApplicationScoped
public class TimeoutResource {

    @TransactionTimeout
    @GET
    public String get() {
        System.out.println("org.sterl.training.trx.timeout.TimeoutResource.get() " + Clock.systemUTC().instant());
        return Clock.systemUTC().instant().toString();
    }
}
