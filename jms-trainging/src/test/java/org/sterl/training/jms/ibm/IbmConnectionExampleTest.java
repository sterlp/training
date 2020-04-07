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
import java.util.concurrent.TimeUnit;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Topic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jms.core.JmsTemplate;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.CMQCFC;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.headers.pcf.PCFMessage;
import com.ibm.mq.headers.pcf.PCFMessageAgent;
import com.ibm.msg.client.jms.JmsConnectionFactory;

class IbmConnectionExampleTest extends AbstractIbmJmsTest {

    private static final String QUEUE_DESTINATION = "DEV.QUEUE.2";

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
