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

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
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
import org.sterl.gcm.smack.GcmPacketExtension;
import org.xmlpull.v1.XmlPullParser;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Setter;

/**
 * Equal like ChatMessageListeningEndpoint but for GCM messages
 */
public class GcmMessageListeningEndpoint extends AbstractXmppConnectionAwareEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(GcmMessageListeningEndpoint.class);
    @Setter
    protected PacketListener packetListener = new GcmPacketListener();
    private ObjectMapper mapper;
    protected XmppHeaderMapper headerMapper = new DefaultXmppHeaderMapper();
    
    @Setter
    protected Class<? extends GcmUpstreamMessage> inboundType = GcmUpstreamMessage.class;
    
    public GcmMessageListeningEndpoint(XMPPConnection connection) {
        super(connection);
        
        // support the JSON GcmPacketExtension for Smack -- otherwise we lose the message
        ProviderManager.addExtensionProvider(GcmPacketExtension.GCM_ELEMENT_NAME, GcmPacketExtension.GCM_NAMESPACE,
                new PacketExtensionProvider() {
                    @Override
                    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
                        String json = parser.nextText();
                        return new GcmPacketExtension(json);
                    }
                });
    }

    class GcmPacketListener implements PacketListener {
        @Override
        public void processPacket(Packet packet) throws NotConnectedException {
            if (packet instanceof org.jivesoftware.smack.packet.Message) {
                final org.jivesoftware.smack.packet.Message xmppMessage = (org.jivesoftware.smack.packet.Message) packet;
                final GcmPacketExtension gcmExtension = (GcmPacketExtension)xmppMessage.getExtension(GcmPacketExtension.GCM_NAMESPACE);
                
                if (gcmExtension == null || StringUtils.hasText(gcmExtension.getJson())) {
                    final Map<String, ?> mappedHeaders = headerMapper.toHeadersFromRequest(xmppMessage);

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
        this.xmppConnection.addPacketListener(this.packetListener, null);
    }

    @Override
    protected void doStop() {
        if (this.xmppConnection != null) {
            this.xmppConnection.removePacketListener(this.packetListener);
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
