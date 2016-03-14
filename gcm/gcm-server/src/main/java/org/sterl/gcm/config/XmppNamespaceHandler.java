/**
 * Copyright 2016 Paul Sterl
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific 
 * language governing permissions and limitations under the License.
 */
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
