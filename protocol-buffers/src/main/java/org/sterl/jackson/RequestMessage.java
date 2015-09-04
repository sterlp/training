package org.sterl.jackson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class RequestMessage {

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class MoveData {
        private int speed;
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
    }
    
    private JacksonType type = JacksonType.MOVE;
    
    private MoveData moveData;
    private TurnData turnData;
    private JumpData jumpData;
}
