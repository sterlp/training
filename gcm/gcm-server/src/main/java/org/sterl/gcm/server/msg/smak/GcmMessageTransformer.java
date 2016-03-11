package org.sterl.gcm.server.msg.smak;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.integration.xmpp.XmppHeaders;
import org.springframework.messaging.Message;
import org.sterl.gcm.server.msg.model.GcmMessage;
import org.sterl.gcm.server.msg.model.GcmNotification;
import org.sterl.gcm.server.msg.model.GcmStringMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Google has an own type of message format, as so we have to map any message to it.
 * 
 * Furthermore this is a good point to do any serialization needed.
 * http://stackoverflow.com/questions/28854835/what-are-the-right-parameters-for-xmpp-connection-spring-integration-to-make-i
 */
public class GcmMessageTransformer extends AbstractTransformer {
    @Autowired ObjectMapper mapper;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected Object doTransform(Message<?> msgIn) throws Exception {
        final Object msg = msgIn.getPayload();

        // NO "TO" HERE!!!! Otherwise GCM routes the message to us for some reason ...
        final org.jivesoftware.smack.packet.Message xmppMessage = new org.jivesoftware.smack.packet.Message();

        GcmMessage message;
        if (msg instanceof GcmMessage) {
            message = (GcmMessage)msg;
        } else {

            final String to = msgIn.getHeaders().get(XmppHeaders.TO, String.class);
            if (null == to) new IllegalArgumentException("Missing receiver, please set header XmppHeaders.TO. (" + XmppHeaders.TO + ")");

            message = new GcmMessage<>(to, UUID.randomUUID().toString());

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
