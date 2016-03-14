package org.sterl.gcm.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.integration.xmpp.config.AbstractXmppInboundChannelAdapterParser;
import org.springframework.integration.xmpp.config.AbstractXmppOutboundChannelAdapterParser;
import org.w3c.dom.Element;

public class XmppNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        // send/receive messages
        registerBeanDefinitionParser("inbound-channel-adapter", new GcmMessageListeningEndpointAdapterParser());
        registerBeanDefinitionParser("outbound-channel-adapter", new GcmSendingMessageHandlerAdapterParser());

    }
    
    public static class GcmMessageListeningEndpointAdapterParser extends AbstractXmppInboundChannelAdapterParser {
        @Override
        protected String getBeanClassName(Element element) {
            return "org.sterl.gcm.inbound.GcmMessageListeningEndpoint";
        }
    }
    public static class GcmSendingMessageHandlerAdapterParser extends AbstractXmppOutboundChannelAdapterParser {
        @Override
        protected String getHandlerClassName() {
            return "org.sterl.gcm.outbound.GcmSendingMessageHandler";
        }
    }
}
