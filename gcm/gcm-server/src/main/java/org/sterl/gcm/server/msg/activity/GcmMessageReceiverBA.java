package org.sterl.gcm.server.msg.activity;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.PacketExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sterl.gcm.server.msg.smak.GcmPacketExtension;

public class GcmMessageReceiverBA {
    private static final Logger LOG = LoggerFactory.getLogger(GcmMessageReceiverBA.class);
    public void handleMessage(Object message) {
        if (message instanceof Message) {
            final Message smakMsg = (Message)message;
            PacketExtension gcmMessageExtension = smakMsg.getExtension(GcmPacketExtension.GCM_NAMESPACE);
            if (gcmMessageExtension != null) {
                LOG.info("Message: {}", gcmMessageExtension);
            } else {
                LOG.warn("Message {} doesn't contain any GCM message!", message);
            }
        }
    }
}
