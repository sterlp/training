package org.sterl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.sterl.wire.JumpData;
import org.sterl.wire.MoveData;
import org.sterl.wire.RequestMessage;
import org.sterl.wire.RequestMessage.MessageType;

import com.squareup.wire.Wire;

public class SerializationWireTest extends AbstractTest{

    @Test
    public void testProto() throws Exception {
        RequestMessage read;
        Wire wire = new Wire();
        for (int i = 0; i < CYCLES; i++) {
            this.buildMessage.start();
            RequestMessage message = new RequestMessage.Builder()
                    .type(MessageType.JUMP)
                    .jumpData(new JumpData.Builder().howFar(10).howHigh(650).build())
                    .moveData(new MoveData.Builder().speed(500).build())
                    .build();
            this.buildMessage.stop();
            
            this.serialization.start();
            byte[] msg = message.toByteArray();
            this.serialization.stop();
            
            this.deserialization.start();
            read = wire.parseFrom(msg, RequestMessage.class);
            this.deserialization.stop();
            
            messageSize = msg.length;
            assertEquals(message, read);
        }
        
        writeStats("Squareup Wire");
    }
}
