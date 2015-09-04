package org.sterl;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.sterl.wire.JumpData;
import org.sterl.wire.MoveData;
import org.sterl.wire.RequestMessage;
import org.sterl.wire.RequestMessage.MessageType;

import com.squareup.wire.Wire;

public class SerializationWireTest extends AbstractTest{

    @Test
    public void testProto() throws Exception {
        long buildMessage = 0;
        long serialization = 0;
        long deserialization = 0;
       
        long time = 0;
        long messageSize = 0;
        
        RequestMessage read;
        Wire wire = new Wire();
        for (int i = 0; i < CYCLES; i++) {
            time = System.nanoTime();
            RequestMessage message = new RequestMessage.Builder()
                    .type(MessageType.JUMP)
                    .jumpData(new JumpData.Builder().howFar(10).howHigh(650).build())
                    .moveData(new MoveData.Builder().speed(500).build())
                    .build();
            buildMessage += System.nanoTime() - time;
            
            time = System.nanoTime();
            byte[] msg = message.toByteArray();
            serialization += System.nanoTime() - time;
            
            time = System.nanoTime();
            read = wire.parseFrom(msg, RequestMessage.class);
            deserialization += System.nanoTime() - time;
            
            messageSize = msg.length;
            assertEquals(message, read);
        }
        
        System.out.println("*** Wire Stats ***");
        writeStats(buildMessage, serialization, deserialization, messageSize);
    }
}
