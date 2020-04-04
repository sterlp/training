package org.sterl.training.jms.ibm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.sterl.training.jms.JmsUtil;
import org.sterl.training.test.AwaitUtil;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.CMQCFC;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.headers.pcf.PCFMessage;
import com.ibm.mq.headers.pcf.PCFMessageAgent;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import lombok.RequiredArgsConstructor;

class IbmConnectionExampleTest {

    private static final String QMGR = "PAUL.DEV";
    private static final String HOST = "paul-dev-eef2.qm.eu-gb.mq.appdomain.cloud";
    private static final int PORT = 30623;
    private static final String CHANNEL = "CLOUD.ADMIN.SVRCONN";
    private static final String QUEUE_DESTINATION = "DEV.QUEUE.2";

    private static final String APP_USER = "";
    private static final String APP_PASSWORD = "";
    
    
    @Test
    public void simpleJmsTemplateSendMessageTest() throws Exception {
        JmsConnectionFactory cf = createConnectionFactory();
        final String queueName = QUEUE_DESTINATION;
        final String msgString = "Simple message " + new Date();
        JmsTemplate jmsTemplate = new JmsTemplate(cf);
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setReceiveTimeout(2500);
        jmsTemplate.setPriority(7);
        jmsTemplate.setExplicitQosEnabled(true);
        jmsTemplate.convertAndSend(queueName, msgString);
        Message msg = jmsTemplate.receive(queueName);
        assertNotNull(msg);
        System.out.println("prio: " + msg.getJMSPriority());
        System.out.println(msg.getBody(String.class));
        assertEquals(msgString, msg.getBody(String.class));
        assertThat(msg.getJMSPriority()).isEqualTo(7);
    }
    
    @Test
    public void simpleConsumerTest() throws Exception {
        final String destination = QUEUE_DESTINATION;
        final String message = QUEUE_DESTINATION + Instant.now();
        
        final CompletableFuture<Message> messageWaiter = new CompletableFuture<>();
        final JmsConnectionFactory cf = createConnectionFactory();
        
        final JMSContext c = cf.createContext();
        final Destination d = c.createQueue(QUEUE_DESTINATION);
        JMSConsumer consumer = c.createConsumer(d);
        consumer.setMessageListener((m) -> {
            System.out.println("onMessage: " + m);
            messageWaiter.complete(m);
        });
        
        JmsTemplate jmsTemplate = new JmsTemplate(cf);
        jmsTemplate.convertAndSend(destination, message);
        
        Message receviedMessage = messageWaiter.get(20, TimeUnit.SECONDS);
        assertThat(receviedMessage.getBody(String.class)).isEqualTo(message);
    }
    
    @Test
    public void simpleTopicTest() throws Exception {
        final JmsConnectionFactory cf = createConnectionFactory();
        final String topicName = "DEV.TOPIC.2";//  "dev/topic/2";
        final String msgString = "Simple message " + new Date();
        final ExecutorService executor = Executors.newFixedThreadPool(1);
        
        try (JMSContext c = cf.createContext()) {
            // first we have to register our consumer
            final Topic topic = c.createTopic(topicName);
            Future<Message> listen = executor.submit(new Callable<Message>() {
                @Override
                public Message call() throws Exception {
                    final JMSConsumer consumer = c.createConsumer(topic);
                    final Message m = consumer.receive(2500);
                    consumer.close();
                    return m;
                }
            });
            
            // publish message to the topic
            c.createProducer().send(topic, msgString);
            
            Message m = listen.get();
            System.out.println(m);
            assertThat(m).isNotNull();
            assertThat(m.getBody(String.class)).isEqualTo(msgString);
        }
        
        executor.shutdownNow();
    }
    
    private JmsConnectionFactory createConnectionFactory() throws JMSException {
        JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
        JmsConnectionFactory cf = ff.createConnectionFactory();
        cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, QMGR);
        cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, HOST);
        cf.setIntProperty(WMQConstants.WMQ_PORT, PORT);
        cf.setStringProperty(WMQConstants.WMQ_CHANNEL, CHANNEL);
        cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutGet (JMS)");
        cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
        cf.setStringProperty(WMQConstants.USERID, APP_USER);
        cf.setStringProperty(WMQConstants.PASSWORD, APP_PASSWORD);
        return cf;
    }

    @DisplayName("List IBM MQ Queues")
    @Test()
    void listQueues() throws Exception {
        Hashtable<String, Object> settings = new Hashtable<>();

        settings.put("hostname", HOST);
        settings.put("port", PORT);
        settings.put("channel", CHANNEL);
        settings.put("userID", APP_USER);
        settings.put("password", APP_PASSWORD);

        try {
            MQQueueManager ibmMqManager = new MQQueueManager(QMGR, settings);
            PCFMessageAgent agent = new PCFMessageAgent(ibmMqManager);

            PCFMessage request = new PCFMessage(CMQCFC.MQCMD_INQUIRE_Q_NAMES);
            request.addParameter(CMQC.MQCA_Q_NAME, "*");
            request.addParameter(CMQC.MQIA_Q_TYPE, CMQC.MQQT_ALL);

            PCFMessage[] response = agent.send(request);
            System.out.println(response.length);
            System.out.println(response[0]);
            
            String[] names = response[0].getStringListParameterValue(MQConstants.MQCACF_Q_NAMES);
            int[] types = response[0].getIntListParameterValue(MQConstants.MQIACF_Q_TYPES);
            
            System.out.println("---------------------");
            for (int i = 0; i < names.length; i++) {
                System.out.println(names[i].trim() + " - " + types[i]);
            }
        } catch (MQException e) {
            System.err.println(e.completionCode + " " + e.reasonCode);
            if (e.getCompCode() == 2 && e.getReason() == 2035) {
                throw new IllegalArgumentException("User has no permissions to list queues. Check the channel " + CHANNEL, e);
            }
            throw e;
        }
    }

    static String getClassType(Object obj) {
        if (obj == null)
            return "None";
        if (obj.getClass().isArray())
            return "Array";
        else
            return obj.getClass().getName();
    }

    static String getValue(Object obj) {
        if (obj == null)
            return null;
        if (obj.getClass().isArray())
            return Arrays.toString((Object[]) obj);
        else
            return obj.toString();
    }

}
