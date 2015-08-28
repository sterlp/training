package org.sterl.education.messgaging;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Controller
public class MessageController {
    
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class Message {
        private Long id;
        private String value;
        private long time;
    }
    
    @MessageMapping("/message")
    @SendTo("/topic/response")
    public Message slowEndpont(Message message) throws Exception {
        Thread.sleep(3000); // simulated delay
        System.err.println("Message Received: " + message);
        return new Message(message.id, "Hello Client", System.currentTimeMillis());
    }

}
