package org.sterl.jackson;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class RequestMessage {

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class MoveData {
        private int speed;
        private String destination;
    }
    @Data @NoArgsConstructor @AllArgsConstructor
    // turn data
    public static class TurnData {
        private int angle;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class JumpData {
        private int howHigh;
        private int howFar;
        private String destination;
    }
    
    private JacksonType type = JacksonType.MOVE;
    private String message1;
    private String message2;
    
    private MoveData moveData;
    private TurnData turnData;
    private JumpData jumpData;
    
    private List<MoveData> movements = new ArrayList<>();
}
