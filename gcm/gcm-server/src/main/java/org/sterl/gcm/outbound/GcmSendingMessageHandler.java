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
package org.sterl.gcm.outbound;

import java.util.UUID;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.gcm.packet.GcmPacketExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.xmpp.XmppHeaders;
import org.springframework.integration.xmpp.core.AbstractXmppConnectionAwareMessageHandler;
import org.springframework.integration.xmpp.support.DefaultXmppHeaderMapper;
import org.springframework.integration.xmpp.support.XmppHeaderMapper;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;
import org.sterl.gcm.api.GcmDownstreamMessage;
import org.sterl.gcm.api.GcmNotification;
import org.sterl.gcm.api.GcmStringMessage;
import org.sterl.gcm.api.GcmXmppHeader;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Like the Spring ChatMessageSendingMessageHandler but with support for GCM messages.
 */
public class GcmSendingMessageHandler extends AbstractXmppConnectionAwareMessageHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GcmSendingMessageHandler.class);

    private ObjectMapper mapper;
    private volatile XmppHeaderMapper headerMapper = new DefaultXmppHeaderMapper();

    public GcmSendingMessageHandler() {
        super();
    }
    public GcmSendingMessageHandler(XMPPConnection xmppConnection) {
        super(xmppConnection);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void handleMessageInternal(Message<?> message) throws Exception {
        Assert.isTrue(this.initialized, this.getComponentName() + "#" + this.getComponentType() + " must be initialized");
        final Object msg = message.getPayload();

        GcmDownstreamMessage downstreamMessage;
        if (msg instanceof GcmDownstreamMessage) {
            downstreamMessage = (GcmDownstreamMessage)msg;
            if (downstreamMessage.getMessageId() == null) throw new IllegalArgumentException("Message ID is required!");
        } else {
            final String to = message.getHeaders().get(XmppHeaders.TO, String.class);
            String messageId = message.getHeaders().get(GcmXmppHeader.MESSAGE_ID, String.class);
            
            if (messageId == null) {
                messageId = UUID.randomUUID().toString();
            }

            downstreamMessage = new GcmDownstreamMessage<>(to, messageId);

            if (msg instanceof GcmNotification) {
                downstreamMessage.setNotification((GcmNotification)msg);
            } else if (msg instanceof String) {
                downstreamMessage.setData(new GcmStringMessage(String.valueOf(msg)));
            } else {
                downstreamMessage.setData(msg);
            }
        }
        if (null == downstreamMessage.getTo()) new IllegalArgumentException("Missing receiver, please set header XmppHeaders.TO. (" + XmppHeaders.TO + ")");

        // NO "TO" HERE!!!! Otherwise GCM routes the message to us for some reason ...
        final org.jivesoftware.smack.packet.Message xmppMessage = new org.jivesoftware.smack.packet.Message();
        xmppMessage.setStanzaId(downstreamMessage.getMessageId());
        final GcmPacketExtension gcmPacketExtension = new GcmPacketExtension(mapper.writeValueAsString(downstreamMessage));
        xmppMessage.addExtension(gcmPacketExtension);

        if (!this.xmppConnection.isConnected() && this.xmppConnection instanceof AbstractXMPPConnection) {
            ((AbstractXMPPConnection) this.xmppConnection).connect();
        }
        LOG.info("Sending GCM message {}", gcmPacketExtension.getJson());
        this.xmppConnection.sendStanza(xmppMessage);
    }
    @Override
    public String getComponentType() {
        return "gcm:outbound-channel-adapter";
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
