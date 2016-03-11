package org.sterl.gcm.server.msg.activity;

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
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.xmpp.inbound.ChatMessageListeningEndpoint;
import org.springframework.integration.xmpp.support.DefaultXmppHeaderMapper;
import org.springframework.integration.xmpp.support.XmppHeaderMapper;
import org.springframework.util.Assert;
import org.sterl.gcm.server.msg.smak.GcmPacketExtension;
import org.xmlpull.v1.XmlPullParser;

import lombok.Setter;

    public class GcmMessageListeningEndpoint extends ChatMessageListeningEndpoint {
        private static final Logger LOG = LoggerFactory.getLogger(GcmMessageListeningEndpoint.class);
        @Setter
        protected PacketListener packetListener = new GcmPacketListener();
        protected XmppHeaderMapper headerMapper = new DefaultXmppHeaderMapper();
        
        public GcmMessageListeningEndpoint(XMPPConnection connection) {
            super(connection);
            
            
            ProviderManager.addExtensionProvider(GcmPacketExtension.GCM_ELEMENT_NAME, GcmPacketExtension.GCM_NAMESPACE,
                    new PacketExtensionProvider() {
                        @Override
                        public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
                            String json = parser.nextText();
                            return new GcmPacketExtension(json);
                        }
                    });
        }
    
        @Override
        public void setHeaderMapper(XmppHeaderMapper headerMapper) {
            super.setHeaderMapper(headerMapper);
            this.headerMapper = headerMapper;
            if (this.headerMapper == null) throw new IllegalArgumentException("Null XmppHeaderMapper isn't supported!");
        }
        
        public String getComponentType() {
            return "xmpp:inbound-channel-adapter-gcm";
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
        
        class GcmPacketListener implements PacketListener {
    
            @Override
            public void processPacket(Packet packet) throws NotConnectedException {
                if (packet instanceof org.jivesoftware.smack.packet.Message) {
                    org.jivesoftware.smack.packet.Message xmppMessage = (org.jivesoftware.smack.packet.Message) packet;
                    Map<String, ?> mappedHeaders = headerMapper.toHeadersFromRequest(xmppMessage);
                    sendMessage(MessageBuilder.withPayload(xmppMessage).copyHeaders(mappedHeaders).build());
                } else {
                    LOG.warn("Unsuported Packet {}", packet);
                }
            }
        }
    }
