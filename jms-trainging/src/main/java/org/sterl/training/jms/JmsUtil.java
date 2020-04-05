package org.sterl.training.jms;

import java.io.Closeable;

import javax.jms.Connection;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.MessageConsumer;
import javax.jms.Session;

public class JmsUtil {

    public static Exception close(MessageConsumer consumer) {
        Exception result = null;
        if (consumer != null) {
            try {
                consumer.close();
            } catch (Exception e) {
                result = e;
            }
        }
        return result;
    }

    public static Exception close(Session session) {
        Exception result = null;
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                result = e;
            }
        }
        return result;
    }

    public static Exception close(Connection connection) {
        Exception result = null;
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                result = e;
            }
        }
        return result;
        
    }

    public static Exception close(JMSContext context) {
        Exception result = null;
        if (context != null) {
            try {
                context.close();
            } catch (Exception e) {
                result = e;
            }
        }
        return result;
    }
    
    public static Exception close(JMSConsumer consomer) {
        Exception result = null;
        if (consomer != null) {
            try {
                consomer.close();
            } catch (Exception e) {
                result = e;
            }
        }
        return result;
    }

    public static Exception close(Closeable toClose) {
        Exception result = null;
        if (toClose != null) {
            try {
                toClose.close();
            } catch (Exception e) {
                result = e;
            }
        }
        return result;
    }
}
