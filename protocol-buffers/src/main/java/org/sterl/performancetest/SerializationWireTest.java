package org.sterl.performancetest;

import java.util.Arrays;

import org.sterl.wire.RequestMessage;
import org.sterl.wire.RequestMessage.*;

import com.squareup.wire.Wire;

/**
 * https://github.com/square/wire
 */
public class SerializationWireTest extends AbstractTest<RequestMessage> {

    public SerializationWireTest(int cycles) {
        super(cycles);
    }
    private Wire wire = new Wire();
    private RequestMessage.Builder builder;
    
    @Override
    protected void beforeTest() {
        builder = new RequestMessage.Builder();
    }
    @Override
    protected RequestMessage buildMessage() throws Exception {
        return builder
                .type(MessageType.JUMP)
                .jumpData(new JumpData.Builder().howFar(numbers[0]).howHigh(numbers[1]).destination(strings[0]) .build())
                .message1(strings[1])
                .message2(strings[2])
                .movements(Arrays.asList(
                        new MoveData.Builder().speed(numbers[2]).destination(strings[3]).build(),
                        new MoveData.Builder().speed(numbers[3]).destination(strings[4]).build()))
                .build();
    }
    
    @Override
    protected byte[] serialization(RequestMessage message) throws Exception {
        return message.toByteArray();
    }
    @Override
    protected RequestMessage deserialization(byte[] data) throws Exception {
        return wire.parseFrom(data, RequestMessage.class);
    }
    @Override
    protected String getName() {
        return "Squareup Wire Statistics";
    }
}
