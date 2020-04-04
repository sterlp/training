package org.sterl.training.jms;

import javax.jms.Connection;
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
}
