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
            this.buildMsg.start();
            RequestMessage message = new RequestMessage.Builder()
                    .type(MessageType.JUMP)
                    .jumpData(new JumpData.Builder().howFar(10).howHigh(650).build())
                    .moveData(new MoveData.Builder().speed(500).build())
                    .build();
            this.buildMsg.stop();
            
            this.serialis.start();
            byte[] msg = message.toByteArray();
            this.serialis.stop();
            
            this.deserial.start();
            read = wire.parseFrom(msg, RequestMessage.class);
            this.deserial.stop();
            
            messageSize = msg.length;
            assertEquals(message, read);
        }
        
        writeStats("Squareup Wire");
    }
}
