package org.sterl.gcm._example.server.config;

import javax.net.ssl.SSLSocketFactory;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
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
        ConnectionConfiguration configuration = new ConnectionConfiguration("gcm-xmpp.googleapis.com", 5235);
        // needs to be enabled -- even if GCM requires TSL ca can't force it with required
        configuration.setSecurityMode(SecurityMode.enabled);
        configuration.setReconnectionAllowed(true);
        // needs to be false for GCM
        configuration.setRosterLoadedAtLogin(false);
        configuration.setSendPresence(false);
        configuration.setSocketFactory(SSLSocketFactory.getDefault());

        XmppConnectionFactoryBean connectionFactoryBean = new XmppConnectionFactoryBean();
        connectionFactoryBean.setConnectionConfiguration(configuration);
        
        connectionFactoryBean.setUser("234377203703@gcm.googleapis.com");
        connectionFactoryBean.setPassword("AIzaSyCySnZ5Ny9jAXtI7Y17co5fv5PSAG9i5-A");

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
