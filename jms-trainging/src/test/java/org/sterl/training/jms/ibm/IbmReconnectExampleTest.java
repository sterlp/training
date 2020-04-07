package org.sterl.training.jms.ibm;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.sterl.training.jms.JmsUtil;
import org.sterl.training.test.AwaitUtil;

import com.ibm.msg.client.jms.JmsConnectionFactory;

import lombok.RequiredArgsConstructor;

/**
 * https://sterl.org/2020/04/reconnecting-jms-listener/ 
 */

//https://www.ibm.com/support/knowledgecenter/en/SSFKSJ_7.5.0/com.ibm.mq.ref.dev.doc/q111980_.htm
// doesn't really work
// cf.setIntProperty(WMQConstants.WMQ_CLIENT_RECONNECT_OPTIONS, WMQConstants.WMQ_CLIENT_RECONNECT); // reconnect!
// cf.setIntProperty(WMQConstants.WMQ_CLIENT_RECONNECT_TIMEOUT, WMQConstants.WMQ_CLIENT_RECONNECT_TIMEOUT_DEFAULT * 100);
class IbmReconnectExampleTest extends AbstractIbmJmsTest {

    private static final String QUEUE_DESTINATION = "DEV.QUEUE.2";
    
    /**
     * For this test open your network settings and be ready to cut the connection between you and your JMS Server.
     * 
     * This means they should be running on the same host of course ...
     */
    @Test
    public void reconnectingConsumerTest() throws Exception {
        final String destination = QUEUE_DESTINATION;
        final String message = "DEV.QUEUE.2 " + Instant.now();
        
        final JmsConnectionFactory cf = createConnectionFactory();
        final ReconnectingListener listener = new ReconnectingListener(cf, QUEUE_DESTINATION);

        listener.connect();

        final JmsTemplate jmsTemplate = new JmsTemplate(cf);
        
        int sendMessageCount = 0;
        boolean hadSendError = false;
        for (int i = 0; i < 15; i++) {
            try {
                jmsTemplate.convertAndSend(destination, message);
                Thread.sleep(10_000);
                ++sendMessageCount;

                if (!hadSendError) System.out.println("\n----> Disbale and Reenable the network connection, go go go. :-)\n");
                else if (i < 13) i = 13;
            } catch (Exception e) {
                hadSendError = true;
                System.out.println("No message send ... " + e.getMessage());
                System.out.println("\n----> Okay enable the network now again ...\n");
                Thread.sleep(10_000);
            }
        }
        AwaitUtil.assertEquals(() -> listener.getReceivedMessageCount(), sendMessageCount);
        listener.disconnect();
    }
    
    /**
     * Very simple class to demonstrate a reconnect to a JMS queue, usually this code is provided by the
     * container e.g. EE or Spring.
     */
    @RequiredArgsConstructor
    static class ReconnectingListener implements MessageListener,  ExceptionListener {
        private static final Logger LOG = LoggerFactory.getLogger(ReconnectingListener.class);
        private final ScheduledExecutorService reconnectScheduler = Executors.newScheduledThreadPool(1);
        private final AtomicBoolean shouldRun = new AtomicBoolean(false); 
        
        // just for demonstration
        private final AtomicInteger receivedMessages = new AtomicInteger(0);
        
        final JmsConnectionFactory cf;
        final String destinationQueue;
        
        private Connection connection;
        private Session session;
        private MessageConsumer consumer;

        public synchronized void connect() throws JMSException {
            shouldRun.set(true);
            if (!isConnected()) {
                connection = cf.createConnection();
                connection.setClientID("Reconnect-Listener");
                session = connection.createSession();
                consumer = session.createConsumer(session.createQueue(destinationQueue));
                consumer.setMessageListener(this); // register us
                connection.setExceptionListener(this); // react on errors
                connection.start();
            }
        }
        public void connectAsync() {
            shouldRun.set(true);
            reconnect(0);
        }
        public synchronized void disconnect() {
            shouldRun.set(false);
            clearConnection();
        }
        /**
         * Stops the connection without a change to the general desire if the connection should run or not.
         */
        private synchronized void clearConnection() {
            JmsUtil.close(consumer);
            JmsUtil.close(session);
            JmsUtil.close(connection);
            consumer = null;
            session = null;
            connection = null;
        }
        @Override
        public void onException(JMSException exception) {
            LOG.warn("{}: Connection lost, will reconnect in 5s. {}", exception.getErrorCode(), exception.getMessage());
            clearConnection();
            reconnect(5);
        }
        /**
         * Tries to connect after the given delay
         */
        private void reconnect(final int delay) {            
            reconnectScheduler.schedule(() -> {
                // only if we should be connected
                if (shouldRun.get()) {
                    try {
                        LOG.debug("Creating new connection ...");
                        connect();
                    } catch (Exception e) {
                        LOG.info("(Re)Connect failed, will retry in {}s. {}", delay, e.getMessage());
                        clearConnection();
                        reconnect(delay);
                    }
                }
            }, delay, TimeUnit.SECONDS);
        }
        @Override
        public void onMessage(Message message) {
            if (receivedMessages.incrementAndGet() % 2 == 0) {
                System.out.println("Throwing error in message ...");
                throw new RuntimeException("Not now I don't like it!");
            } else {
                LOG.info("onMessage: {}", message);
            }
        }
        public boolean isConnected() {
            return connection != null && consumer != null && session != null;
        }
        public int getReceivedMessageCount() {
            return receivedMessages.get();
        }
    }
}
