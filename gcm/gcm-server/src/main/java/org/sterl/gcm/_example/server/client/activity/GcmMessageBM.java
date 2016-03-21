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
package org.sterl.gcm._example.server.client.activity;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.xmpp.XmppHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;
import org.sterl.gcm.api.GcmDownstreamMessage;
import org.sterl.gcm.api.GcmNotification;
import org.sterl.gcm.api.GcmUpstreamMessage;
import org.sterl.gcm.api.GcmXmppHeader;

/**
 * Our message handler for GCM messages, which send messages and returns {@link Future} objects we can listen on.
 */
@Component
public class GcmMessageBM {

    @Autowired private GcmMessageReceiverBA receiverBA;
    @Autowired private MessageChannel gcmOutbound;

    public Future<GcmUpstreamMessage> sendNotification(String to, String notification) {
        return sendNotification(to, new GcmNotification(null, notification, null));
    }
    public Future<GcmUpstreamMessage> sendNotification(String to, GcmNotification notification) {
        CompletableFuture<GcmUpstreamMessage> result = new CompletableFuture<>();
        
        final String messageId = UUID.randomUUID().toString();
        if (gcmOutbound.send(MessageBuilder.withPayload(notification)
                .setHeader(XmppHeaders.TO, to)
                .setHeader(GcmXmppHeader.MESSAGE_ID, messageId).build())) {
            receiverBA.waitFor(messageId, result);
        } else {
            result.completeExceptionally(new IOException("Failed to send notification " + notification + " to: " + to));
        }
        return result;
    }
    public <PayloadType> Future<GcmUpstreamMessage> send(String to, PayloadType payload) {
        CompletableFuture<GcmUpstreamMessage> result = new CompletableFuture<>();
        
        final String messageId = UUID.randomUUID().toString();
        if (gcmOutbound.send(MessageBuilder.withPayload(new GcmDownstreamMessage<PayloadType>(to, messageId, payload)).build())) {
            receiverBA.waitFor(messageId, result);
        } else {
            result.completeExceptionally(new IOException("Failed to send message " + payload + " to: " + to));
        }
        return result;
    }
}