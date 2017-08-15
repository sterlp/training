package org.sterl.gcm._example.server.client;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sterl.gcm._example.server.client.activity.GcmMessageBM;
import org.sterl.gcm._example.server.client.dao.GcmClientDAO;
import org.sterl.gcm._example.server.client.model.GcmClientBE;
import org.sterl.gcm.api.GcmStringMessage;
import org.sterl.gcm.api.GcmUpstreamMessage;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@RequestMapping("/gcmSend")
public class SenderBF {
    private static final Logger LOG = LoggerFactory.getLogger(SenderBF.class);
    @Data
    static class SendResult {
        /** thats not the ACK from google here, as we receive the ack on the inbound channel we would need to create a future to collect it .. */
        private int clientSuccess = 0;
        private int clientFailed = 0;
        
        public void failed() {
            ++clientFailed;
        }
        public void success() {
            ++clientSuccess;
        }
    }
    @Data @AllArgsConstructor
    static class MessageData {
        private String message;
    }
    @Autowired private GcmMessageBM messageBM;
    @Autowired private GcmClientDAO clientDao;
    
    @PostConstruct
    public void init() {
        clientDao.save(new GcmClientBE("dUXsJW01LzI:APA91bG8x17P9l7uDUvVMKhQ9nZjgasRNE4p8Py86Z3v6C0hlzZva-WDJISWhbrNseWe5zC8IprujZ8cRJB7qrw1vMIg2TWwHUIcCiDOzoccRDbEEZEGP-vCwiGiOBzTS7vTuC6vaBgo"));
    }
    
    
    /** Sends a notification which is displayed by android by default */
    @RequestMapping("/notification")
    public SendResult sendNotification(@RequestParam(required = false) String message) throws JsonProcessingException {
        if (message == null) message = "Test Message " + new Date();
        
        final SendResult result = new SendResult();
        final List<Future<GcmUpstreamMessage>> waitingFor = new LinkedList<>();
        for (GcmClientBE client : clientDao.findAll()) {
            waitingFor.add(messageBM.sendNotification(client.getId(), message));
        }

        waitForResponses(result, waitingFor);
        return result;
    }


    /** Sends a normal messages which contains in the data object a text attribute we can read */
    @RequestMapping("/message")
    public SendResult sendMessage(@RequestParam(required = false) String message) throws JsonProcessingException {
        if (message == null) message = "Test Message " + new Date();
        
        final SendResult result = new SendResult();
        final List<Future<GcmUpstreamMessage>> waitingFor = new LinkedList<>();
        for (GcmClientBE client : clientDao.findAll()) {
            waitingFor.add(messageBM.send(client.getId(), new GcmStringMessage(message)));
        }

        waitForResponses(result, waitingFor);
        
        return result;
    }
    
    protected void waitForResponses(final SendResult result, List<Future<GcmUpstreamMessage>> waitingFor) {
        for (Future<GcmUpstreamMessage> future : waitingFor) {
            try {
                GcmUpstreamMessage gcmResponse = future.get(5, TimeUnit.SECONDS);
                if ("ack".equals(gcmResponse.getMessageType())) {
                    ++result.clientSuccess;
                } else {
                    ++result.clientFailed;
                }
                
            } catch (Exception e) {
                ++result.clientFailed;
                LOG.warn("GCM Message failed: " + e.getMessage());
            }
        }
    }
}
