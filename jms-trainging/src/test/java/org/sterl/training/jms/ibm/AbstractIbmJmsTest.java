package org.sterl.training.jms.ibm;

import javax.jms.JMSException;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

class AbstractIbmJmsTest {

    protected static final String QMGR = "PAUL.DEV";
    protected static final String HOST = "paul-dev-eef2.qm.eu-gb.mq.appdomain.cloud";
    protected static final int PORT = 30623;
    protected static final String CHANNEL = "CLOUD.ADMIN.SVRCONN";
    
    protected static final String APP_USER = "";
    protected static final String APP_PASSWORD = "";

    protected JmsConnectionFactory createConnectionFactory() throws JMSException {
        JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
        JmsConnectionFactory cf = ff.createConnectionFactory();
        cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, QMGR);
        cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, HOST);
        cf.setIntProperty(WMQConstants.WMQ_PORT, PORT);
        cf.setStringProperty(WMQConstants.WMQ_CHANNEL, CHANNEL);
        cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JMS Training Test Client");
        cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
        cf.setStringProperty(WMQConstants.USERID, APP_USER);
        cf.setStringProperty(WMQConstants.PASSWORD, APP_PASSWORD);
        return cf;
    }
}
