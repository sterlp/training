package org.sterl.education.messgaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestEndpoint {

    @Autowired
    SimpMessagingTemplate messageTemplate;
    @Autowired
    MessageController c;
    
    @RequestMapping("/rest/send/{id}")
    public long send(@PathVariable long id) {
        
        MessageController.Message msg = new MessageController.Message(1L, "Hello Server", System.currentTimeMillis());
        messageTemplate.convertAndSend("/app/message", 
                msg);
        
        messageTemplate.convertAndSend("/topic/message", 
                msg);
        
        messageTemplate.convertAndSend("/queue/message", 
                msg);
        
        messageTemplate.convertAndSend("/message", 
                msg);
        return msg.getTime();
    }
    
    @RequestMapping("/rest/message")
    public void send() {
        c.send();
    }
}
