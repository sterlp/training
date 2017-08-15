package org.sterl.performancetest;

import org.sterl.proto.SocketRequest;
import org.sterl.proto.SocketRequest.RequestMessage;
import org.sterl.proto.SocketRequest.RequestMessage.JumpData;
import org.sterl.proto.SocketRequest.RequestMessage.MessageType;
import org.sterl.proto.SocketRequest.RequestMessage.MoveData;
/**
 * https://developers.google.com/protocol-buffers/docs/proto
 */
public class SerializationProtoTest extends AbstractTest<RequestMessage> {

    public SerializationProtoTest(int cycles) {
        super(cycles);
    }
    SocketRequest.RequestMessage.Builder builder = SocketRequest.RequestMessage.newBuilder();
    JumpData.Builder jumpBulder = JumpData.newBuilder();
    MoveData.Builder moveBuilder = MoveData.newBuilder();

    @Override
    protected void beforeTest() {
        builder.clear();
        jumpBulder.clear();
        moveBuilder.clear();
    }
    
    @Override
    protected RequestMessage buildMessage() throws Exception {
        return builder
                .setType(MessageType.JUMP)
                .setJumpData(jumpBulder.setHowFar(numbers[0]).setHowHigh(numbers[1]).setDestination(strings[0]))
                .setMessage1(strings[1])
                .setMessage2(strings[2])
                .addMovements(moveBuilder.setDestination(strings[3]).setSpeed(numbers[3]))
                .addMovements(moveBuilder.setDestination(strings[4]).setSpeed(numbers[4]))
                .build();
    }
    @Override
    protected byte[] serialization(RequestMessage msg) throws Exception {
        return msg.toByteArray();
    }
    @Override
    protected RequestMessage deserialization(byte[] data) throws Exception {
        return RequestMessage.parseFrom(data);
    }
    @Override
    protected String getName() {
        return "Protocol Buffers Statistics";
    }
}
