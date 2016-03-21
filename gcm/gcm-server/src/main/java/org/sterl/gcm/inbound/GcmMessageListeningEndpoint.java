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
package org.sterl.gcm.inbound;

import java.util.Map;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.gcm.packet.GcmPacketExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.xmpp.core.AbstractXmppConnectionAwareEndpoint;
import org.springframework.integration.xmpp.support.DefaultXmppHeaderMapper;
import org.springframework.integration.xmpp.support.XmppHeaderMapper;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.sterl.gcm.api.GcmUpstreamMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Setter;

/**
 * Equal like ChatMessageListeningEndpoint but for GCM messages
 * 
 * Can be replaced with:
 * <pre>
 * {@code
 *  <int:channel id="gcmInbound" />
 *  <int-xmpp:inbound-channel-adapter channel="gcmInbound" payload-expression="getExtension('google:mobile:data').json" xmpp-connection="gcmConnection" />
 *  <bean id="gcmMessageReceiverBA" class="org.sterl.gcm._example.server.client.activity.GcmMessageReceiverBA" />
 *  <int:service-activator input-channel="gcmInbound" ref="gcmMessageReceiverBA" method="handleMessage" />
 *  }
 * </pre>
 */
public class GcmMessageListeningEndpoint extends AbstractXmppConnectionAwareEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(GcmMessageListeningEndpoint.class);
    @Setter
    protected StanzaListener stanzaListener = new GcmPacketListener();
    private ObjectMapper mapper;
    protected XmppHeaderMapper headerMapper = new DefaultXmppHeaderMapper();
    
    @Setter
    protected Class<? extends GcmUpstreamMessage> inboundType = GcmUpstreamMessage.class;
    
    public GcmMessageListeningEndpoint(XMPPConnection connection) {
        super(connection);
    }

    class GcmPacketListener implements StanzaListener {
        @Override
        public void processPacket(Stanza packet) throws NotConnectedException {
            if (packet instanceof org.jivesoftware.smack.packet.Message) {
                final org.jivesoftware.smack.packet.Message xmppMessage = (org.jivesoftware.smack.packet.Message) packet;
                final GcmPacketExtension gcmExtension = (GcmPacketExtension)xmppMessage.getExtension(GcmPacketExtension.NAMESPACE);
                
                if (gcmExtension == null || StringUtils.hasText(gcmExtension.getJson())) {
                    final Map<String, ?> mappedHeaders = headerMapper.toHeadersFromRequest(xmppMessage);

                    // TODO ACK / NACK handling here? or add this to the message header?

                    try {
                        final GcmUpstreamMessage obj = mapper.readValue(gcmExtension.getJson(), inboundType);
                        sendMessage(MessageBuilder.withPayload(obj).copyHeaders(mappedHeaders).build());
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse value " + gcmExtension.getJson(), e);
                    }
                } else {
                    LOG.warn("Received empty or missing GCM extensions {} in message: {}. Message will be ignored!", gcmExtension, packet);
                }
            } else {
                LOG.warn("Unsuported Packet {}", packet);
            }
        }
    }

    @Override
    protected void doStart() {
        Assert.isTrue(this.initialized, this.getComponentName() + " [" + this.getComponentType() + "] must be initialized");
        this.xmppConnection.addAsyncStanzaListener(this.stanzaListener, null);
    }

    @Override
    protected void doStop() {
        if (this.xmppConnection != null) {
            this.xmppConnection.removeAsyncStanzaListener(this.stanzaListener);
        }
    }

    public String getComponentType() {
        return "gcm:inbound-channel-adapter";
    }

    public void setHeaderMapper(XmppHeaderMapper headerMapper) {
        Assert.notNull(headerMapper, "headerMapper must not be null");
        this.headerMapper = headerMapper;
    }
    @Autowired
    public void setMapper(ObjectMapper mapper) {
        Assert.notNull(headerMapper, "ObjectMapper must not be null");
        this.mapper = mapper;
    }
}
