package org.sterl.education.messgaging;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MessagingApplication.class)
public class MessagingApplicationTests {

    @Autowired
    SimpMessagingTemplate messageTemplate;
    @Test
    public void contextLoads() {
    }
    
    @Test
    public void sendMessage() throws Exception {
        System.err.println("** Sending messages...");
        messageTemplate.convertAndSend("/app/message", 
                new MessageController.Message(1L, "Hello Server", System.currentTimeMillis()));
        
        messageTemplate.convertAndSend("/topic/message", 
                new MessageController.Message(1L, "Hello Server", System.currentTimeMillis()));
        
        messageTemplate.convertAndSend("/queue/message", 
                new MessageController.Message(1L, "Hello Server", System.currentTimeMillis()));
        
        messageTemplate.convertAndSend("/message", 
                new MessageController.Message(1L, "Hello Server", System.currentTimeMillis()));
        System.err.println("** Messages send!");
        Thread.sleep(1500);
    }
}
