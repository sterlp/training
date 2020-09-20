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
        TransactionTimeout timeout = getAnnoation(ctx);
        Object currentValue = em.getProperties().get("javax.persistence.query.timeout");
        try {
            System.out.println(em.getClass().getSimpleName() + "--> setProperty " + timeout.value() * 1000 + " before: " + currentValue);
            em.setProperty("javax.persistence.query.timeout", timeout.value() * 1000);
            return ctx.proceed();
        } finally {
            em.setProperty("javax.persistence.query.timeout", currentValue);
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