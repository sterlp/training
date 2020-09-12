package org.sterl.traning.ee.trx.timeout;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.interceptor.*;
import javax.transaction.Transactional;

/**
 * 
 *
 * @author Paul Sterl
 */
@TransactionTimeout
@Interceptor
//@Priority(Interceptor.Priority.PLATFORM_BEFORE + 199)
public class TransactionTimeoutInterceptor implements Serializable {
    private static final Logger _logger = Logger.getLogger(TransactionTimeoutInterceptor.class.getSimpleName());
    
    @AroundInvoke
    public Object execute(InvocationContext ctx) throws Exception {
        System.out.println("*** org.sterl.traning.ee.trx.timeout.TransactionTimeoutInterceptor.execute()");
        return ctx.proceed();
    }
}
