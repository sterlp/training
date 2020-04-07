package org.sterl.training.jms.ibm;

import java.time.Duration;

import javax.jms.JMSContext;

import org.junit.jupiter.api.Test;
import org.sterl.training.test.AwaitUtil;

import com.ibm.msg.client.jms.JmsConnectionFactory;

/**
 * https://www.ibm.com/support/knowledgecenter/SSFKSJ_8.0.0/com.ibm.mq.pro.doc/q005020_.htm
 */
class IbmSubscriptionStringExampleTest extends AbstractIbmJmsTest {

    /**
     * This test should demonstrate how topic strings works and may be
     * utilized in a event source system attaching an audit service.
     * 
     * Create the following topics:
     * <ul>
     *  <li>Order Topic:</li>
     *  <ul>
     *      <li>Topic Name:   APP.TOPIC.EVENTS.ORDER_SYSTEM_NAME
     *      <li>Topic String: APP/EVENTS/ORDERS
     *  </ul>
     *  <li>Payment Topic:</li>
     *  <ul>
     *      <li>Topic Name:   APP.TOPIC.EVENTS.PAYMENT_SYSTEM_NAME
     *      <li>Topic String: APP/EVENTS/PAYMENTS
     *  </ul>
     * </ul>
     * <p>
     * <strong>Note:</strong> This example removed the versions from the topic string.
     * </p>
     */
    @Test
    public void topicStringTest() throws Exception {
        final JmsConnectionFactory cf = createConnectionFactory();
        
        final ReconnectingListenerJms2 auditService = new ReconnectingListenerJms2(cf, "APP/EVENTS/+").connectAsync();
        final ReconnectingListenerJms2 orderService = new ReconnectingListenerJms2(cf, "APP/EVENTS/ORDERS").connectAsync();
        final ReconnectingListenerJms2 paymentService = new ReconnectingListenerJms2(cf, "APP/EVENTS/PAYMENTS").connectAsync();
        
        AwaitUtil.assertEquals(() -> auditService.isConnected() && orderService.isConnected() && paymentService.isConnected(), true, Duration.ofSeconds(30));
        
        try (JMSContext c = cf.createContext()) {
            // assume we had provided a correlation id in the JMS header for this two events, that the audit service can correlate them
            c.createProducer().send(c.createTopic("APP/EVENTS/ORDERS"), "Order 1 was ordered!");
            c.createProducer().send(c.createTopic("APP/EVENTS/PAYMENTS"), "Order 1 was payed!");
        }

        AwaitUtil.assertEquals(() -> orderService.getReceivedMessageCount(), 1, Duration.ofSeconds(5));
        AwaitUtil.assertEquals(() -> paymentService.getReceivedMessageCount(), 1, Duration.ofSeconds(5));
        // our audit service should receive both messages
        AwaitUtil.assertEquals(() -> auditService.getReceivedMessageCount(), 2, Duration.ofSeconds(5));

        auditService.disconnect();
        orderService.disconnect();
        paymentService.disconnect();
    }
}
