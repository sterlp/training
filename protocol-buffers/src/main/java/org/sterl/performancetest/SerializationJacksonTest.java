package org.sterl.performancetest;

import org.sterl.jackson.JacksonType;
import org.sterl.jackson.RequestMessage;
import org.sterl.jackson.RequestMessage.JumpData;
import org.sterl.jackson.RequestMessage.MoveData;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializationJacksonTest extends AbstractTest<RequestMessage> {

    public SerializationJacksonTest(int cycles) {
        super(cycles);
    }

    private ObjectMapper mapper = new ObjectMapper();
    
    @Override
    protected void beforeTest() {
    }
    
    @Override
    protected RequestMessage buildMessage() {
        RequestMessage message = new RequestMessage();
        message.setType(JacksonType.JUMP);
        message.setJumpData(new JumpData(numbers[0], numbers[1], strings[0]));
        message.setMessage1(strings[1]);
        message.setMessage2(strings[2]);
        message.getMovements().add(new MoveData(numbers[2], strings[3]));
        message.getMovements().add(new MoveData(numbers[3], strings[4]));
        return message;
    }
    @Override
    protected byte[] serialization(RequestMessage message) throws Exception {
        return mapper.writeValueAsBytes(message);
    }
    
    @Override
    protected RequestMessage deserialization(byte[] data) throws Exception {
        return mapper.readValue(data, RequestMessage.class);
    }
    @Override
    protected String getName() {
        return "Jackson Statistics";
    }
}
