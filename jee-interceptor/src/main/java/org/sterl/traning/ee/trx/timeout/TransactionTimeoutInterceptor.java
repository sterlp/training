package org.sterl.traning.ee.trx.timeout;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.annotation.Resource;
import javax.interceptor.*;
import javax.transaction.TransactionManager;

/**
 * Setting a timeout the TransactionManager only sets rollback only, but does not cancel the transaction.
 *
 * @author Paul Sterl
 */
@TransactionTimeout
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE + 199)
public class TransactionTimeoutInterceptor implements Serializable {
    private static final Logger _logger = Logger.getLogger(TransactionTimeoutInterceptor.class.getSimpleName());

    @Resource(lookup = "java:appserver/TransactionManager") TransactionManager transactionManager;

    @AroundInvoke
    public Object execute(InvocationContext ctx) throws Exception {
        Object result;
        // no transaction is currently running
        final TransactionTimeout annoation = getAnnoation(ctx);
        final int timeout = annoation == null ? 0 : annoation.value();
        if (timeout > 0) {
            if (transactionManager.getTransaction() != null) {
                _logger.fine("Transaction already running, timeout will only be applied to the next transaction.");
            }
            try {
                transactionManager.setTransactionTimeout(timeout);
                result = ctx.proceed();
            } finally {
                // restore the default
                transactionManager.setTransactionTimeout(0);
            }
   
        } else {
            result = ctx.proceed();
        }
        return result;
    }
    
    private TransactionTimeout getAnnoation(InvocationContext ctx) {
        TransactionTimeout result = ctx.getMethod().getAnnotation(TransactionTimeout.class);
        if (result == null) {
            result = ctx.getTarget().getClass().getAnnotation(TransactionTimeout.class);
        }
        return result;
    }
}