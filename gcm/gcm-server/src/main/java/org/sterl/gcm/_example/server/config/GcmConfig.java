package org.sterl.gcm._example.server.config;

import javax.net.ssl.SSLSocketFactory;

import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.xmpp.config.XmppConnectionFactoryBean;

@Configuration
@ImportResource("classpath:gcm-xmpp-beans.xml")
public class GcmConfig {

    @Bean
    public XmppConnectionFactoryBean gcmConnection(){
        // using prod as pre-prod sometimes just didn't work
        // https://developers.google.com/cloud-messaging/ccs#connecting
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
            .setHost("gcm-xmpp.googleapis.com")
            .setPort(5235)
            .setSecurityMode(SecurityMode.ifpossible)
            .setSendPresence(false)
            .setCompressionEnabled(true)
            .setSocketFactory(SSLSocketFactory.getDefault())
            .setServiceName("gcm-sample")
            .setUsernameAndPassword("234377203703@gcm.googleapis.com", "AIzaSyCySnZ5Ny9jAXtI7Y17co5fv5PSAG9i5-A").build();
        XmppConnectionFactoryBean connectionFactoryBean = new XmppConnectionFactoryBean();
        
        connectionFactoryBean.setConnectionConfiguration(config);

        return connectionFactoryBean;
    }
    /**
    @Bean 
    public GcmMessageListeningEndpoint inboundAdpater(XMPPConnection connection, MessageChannel gcmInbound) {
        GcmMessageListeningEndpoint endpoint = new GcmMessageListeningEndpoint(connection);
        endpoint.setOutputChannel(gcmInbound);
        endpoint.setAutoStartup(true);
        return endpoint;
    }
    */
}
