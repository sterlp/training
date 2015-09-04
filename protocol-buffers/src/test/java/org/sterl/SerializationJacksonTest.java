package org.sterl;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.omg.CosNaming.NamingContextExtOperations;
import org.sterl.jackson.JacksonType;
import org.sterl.jackson.RequestMessage;
import org.sterl.jackson.RequestMessage.JumpData;
import org.sterl.jackson.RequestMessage.MoveData;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializationJacksonTest extends AbstractTest {

    ObjectMapper mapper = new ObjectMapper();
    
    @Test
    public void serlialization() throws Exception {
        
        long buildMessage = 0;
        long serialization = 0;
        long deserialization = 0;
       
        long time = 0;
        long messageSize = 0;
        
        RequestMessage read = null;
        for (int i = 0; i < CYCLES; i++) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            time = System.nanoTime();
            RequestMessage message = new RequestMessage();
            message.setType(JacksonType.JUMP);
            message.setJumpData(new JumpData(10, 650));
            message.setMoveData(new MoveData(500));
            buildMessage += System.nanoTime() - time;
            
            time = System.nanoTime();
            mapper.writeValue(out, message);
            serialization += System.nanoTime() - time;
            
            byte[] msg = out.toByteArray();
            time = System.nanoTime();
            read = mapper.readValue(msg, RequestMessage.class);
            deserialization += System.nanoTime() - time;
            
            messageSize = msg.length;
            assertEquals(message, read);
        }

        System.out.println("*** Jackson Stats ***");
        writeStats(buildMessage, serialization, deserialization, messageSize);
    }
}
