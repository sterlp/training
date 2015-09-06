package org.sterl;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.sterl.proto.TestMessage;
import org.sterl.proto.TestMessage.JumpData;
import org.sterl.proto.TestMessage.MoveData;
import org.sterl.proto.TestMessage.RequestMessage;
import org.sterl.proto.TestMessage.RequestMessage.Builder;
import org.sterl.proto.TestMessage.RequestMessage.MessageType;

public class SerializationProtoTest extends AbstractTest{

    @Test
    public void testProto() throws Exception {
        RequestMessage read;
        Builder newBuilder = TestMessage.RequestMessage.newBuilder();
        org.sterl.proto.TestMessage.JumpData.Builder jumpBulder = JumpData.newBuilder();
        org.sterl.proto.TestMessage.MoveData.Builder moveBuilder = MoveData.newBuilder();
        for (int i = 0; i < CYCLES; i++) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            this.buildMsg.start();
            RequestMessage message = newBuilder
                    .setType(MessageType.JUMP)
                    .setJumpData(jumpBulder.setHowFar(10).setHowHigh(650))
                    .setMoveData(moveBuilder.setSpeed(500))
                    .build();
            newBuilder.clear();
            this.buildMsg.stop();
            
            this.serialis.start();
            message.writeTo(out);
            this.serialis.stop();
            
            byte[] msg = out.toByteArray();
            this.deserial.start();
            read = RequestMessage.parseFrom(msg) ;
            this.deserial.stop();
            
            messageSize = msg.length;
            assertEquals(message, read);
        }
        
        writeStats("Protocol Buffers Statistics");
    }
}
