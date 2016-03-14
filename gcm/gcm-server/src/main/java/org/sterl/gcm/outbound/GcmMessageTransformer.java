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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.integration.xmpp.XmppHeaders;
import org.springframework.messaging.Message;
import org.sterl.gcm.api.GcmDownstreamMessage;
import org.sterl.gcm.api.GcmNotification;
import org.sterl.gcm.api.GcmStringMessage;
import org.sterl.gcm.smack.GcmPacketExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Google has an own type of message format, as so we have to map any message to it.
 * 
 * Furthermore this is a good point to do any serialization needed.
 * 
 * <b>Note: This class is only needed if GcmSendingMessageHandler isn't used.</b>
 * 
 * http://stackoverflow.com/questions/28854835/what-are-the-right-parameters-for-xmpp-connection-spring-integration-to-make-i
 */
@Deprecated
public class GcmMessageTransformer extends AbstractTransformer {
    @Autowired ObjectMapper mapper;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected Object doTransform(Message<?> msgIn) throws Exception {
        final Object msg = msgIn.getPayload();

        // NO "TO" HERE!!!! Otherwise GCM routes the message to us for some reason ...
        final org.jivesoftware.smack.packet.Message xmppMessage = new org.jivesoftware.smack.packet.Message();

        GcmDownstreamMessage message;
        if (msg instanceof GcmDownstreamMessage) {
            message = (GcmDownstreamMessage)msg;
        } else {

            final String to = msgIn.getHeaders().get(XmppHeaders.TO, String.class);
            if (null == to) new IllegalArgumentException("Missing receiver, please set header XmppHeaders.TO. (" + XmppHeaders.TO + ")");

            message = new GcmDownstreamMessage<>(to, UUID.randomUUID().toString());

            if (msg instanceof GcmNotification) {
                message.setNotification((GcmNotification)msg);
            } else if (msg instanceof String) {
                message.setData(new GcmStringMessage(String.valueOf(msg)));
            } else {
                message.setData(msg);
            }
        }
        xmppMessage.setPacketID(message.getMessageId());
        xmppMessage.addExtension(new GcmPacketExtension(mapper.writeValueAsString(message)));

        return xmppMessage;
    }
}
