package org.sterl.training.jms.ibm;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.ExceptionListener;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sterl.training.jms.JmsUtil;
import org.sterl.training.jms.ibm.IbmReconnectExampleTest.ReconnectingListener;

import com.ibm.msg.client.jms.JmsConnectionFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReconnectingListenerJms2 implements MessageListener,  ExceptionListener {
    private static final Logger LOG = LoggerFactory.getLogger(ReconnectingListener.class);
    private final ScheduledExecutorService reconnectScheduler = Executors.newScheduledThreadPool(1);
    private final AtomicBoolean shouldRun = new AtomicBoolean(false); 
    
    // just for demonstration
    private final AtomicInteger receivedMessages = new AtomicInteger(0);
    
    private final JmsConnectionFactory cf;
    private final String destinationQueue;
    
    private JMSContext context;
    private JMSConsumer consumer;

    public synchronized void connect() throws JMSException {
        shouldRun.set(true);
        if (!isConnected()) {
            context = cf.createContext();
            context.setExceptionListener(this); // react on errors
            context.setAutoStart(true);
            consumer = context.createConsumer(context.createQueue(destinationQueue));
            consumer.setMessageListener(this); // register us
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
        JmsUtil.close(context);
        JmsUtil.close(consumer);
        context = null;
        consumer = null;
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
        LOG.info("onMessage: {}", message);
        receivedMessages.incrementAndGet();
    }
    public boolean isConnected() {
        return context != null && consumer != null;
    }
    public int getReceivedMessageCount() {
        return receivedMessages.get();
    }
}
