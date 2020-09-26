package org.sterl.traning.ee.trx.timeout;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.interceptor.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Setting timeout to the JDBC connection, to ensure we cancel any long running queries.
 * 
 * @author Paul Sterl
 */
@TransactionTimeout
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE + 201)
public class ConnectionTimeoutInterceptor implements Serializable {
    private static final Logger _logger = Logger.getLogger(ConnectionTimeoutInterceptor.class.getSimpleName());

    @PersistenceContext EntityManager em;
    
    @AroundInvoke
    public Object execute(InvocationContext ctx) throws Exception {
        final TransactionTimeout timeoutAnnotation = getAnnoation(ctx);
        
        if (timeoutAnnotation != null && timeoutAnnotation.value() > 0) {
            final long timeout = timeoutAnnotation.value() * 1000L;
            final Object currentValue = em.getProperties().get("javax.persistence.query.timeout");
            try {
                System.out.println("setProperty: " + timeout + " before: " + currentValue);
                em.setProperty("javax.persistence.query.timeout", timeout + "");
                // eclipselink doesn't work https://github.com/eclipse-ee4j/eclipselink/issues/912
                //em.setProperty("eclipselink.jdbc.timeout", timeoutAnnotation.value());
                //em.setProperty("eclipselink.query.timeout.unit", "MILLISECONDS");
                return ctx.proceed();
            } finally {
                em.setProperty("javax.persistence.query.timeout", currentValue);
            }
        } else {
            return ctx.proceed();
        }
    }
    
    private TransactionTimeout getAnnoation(InvocationContext ctx) {
        TransactionTimeout result = ctx.getMethod().getAnnotation(TransactionTimeout.class);
        if (result == null) {
            result = ctx.getTarget().getClass().getAnnotation(TransactionTimeout.class);
        }
        return result;
    }
}