package org.sterl.gcm._example.server.client.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sterl.gcm.api.GcmUpstreamMessage;

public class GcmMessageReceiverBA {
    private static final Logger LOG = LoggerFactory.getLogger(GcmMessageReceiverBA.class);
    
    public void handleMessage(GcmUpstreamMessage message) {
        LOG.info("handleMessage: {}", message);
    }
}
