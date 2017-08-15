package org.sterl.gcm._example.server.client.activity;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.sterl.gcm.api.GcmUpstreamMessage;

import lombok.AllArgsConstructor;

public class GcmMessageReceiverBA {
    private static final Logger LOG = LoggerFactory.getLogger(GcmMessageReceiverBA.class);

    @AllArgsConstructor
    private static class Waiter {
        public final long timeout;
        public final CompletableFuture<GcmUpstreamMessage> waiter;
    }
    
    private final ConcurrentMap<String, Waiter> waiters = new ConcurrentHashMap<>();
    
    public void handleMessage(GcmUpstreamMessage message) {
        LOG.info("handleMessage: {}", message);
        final Waiter waiter = waiters.get(message.getMessageId());
        if (waiter != null) {
            waiter.waiter.complete(message);
            waiters.remove(message.getMessageId());
        } else {
            // TODO send ack for received messages and dispatch them to the right handler ...?
        }
    }

    public void waitFor(String messageId, CompletableFuture<GcmUpstreamMessage> waiter) {
        waiters.put(messageId, new Waiter(System.currentTimeMillis() + 10000, waiter));
    }

    @Scheduled(fixedRate = 30000)
    void clean() {
        List<Entry<String, Waiter>> timeouts = waiters.entrySet().stream().filter(e -> e.getValue().timeout > System.currentTimeMillis()).collect(Collectors.toList());
        for (Entry<String, Waiter> entry : timeouts) {
            entry.getValue().waiter.completeExceptionally(new TimeoutException("No result in " + entry.getValue().timeout + " received!"));
            waiters.remove(entry.getKey());
        }
    }
}
