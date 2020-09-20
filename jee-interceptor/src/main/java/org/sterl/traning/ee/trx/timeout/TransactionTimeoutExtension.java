package org.sterl.traning.ee.trx.timeout;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

/**
 * Enables the interceptor using the Java SPI interface.
 * 
 * @author Paul Sterl
 */
public class TransactionTimeoutExtension implements Extension {

    // This is only needed if this extension isn't part of the project, e.g. an included jar
    public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery beforeBeanDiscoveryEvent, final BeanManager beanManager) {
        // AnnotatedType<TransactionTimeoutInterceptor> interceptor = beanManager.createAnnotatedType(TransactionTimeoutInterceptor.class);
        // beforeBeanDiscoveryEvent.addAnnotatedType(interceptor, TransactionTimeoutInterceptor.class.getName());
    }
}
