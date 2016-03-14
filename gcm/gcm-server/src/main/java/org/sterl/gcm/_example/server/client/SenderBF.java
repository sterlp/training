package org.sterl.gcm._example.server.client;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.xmpp.XmppHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sterl.gcm._example.server.client.dao.GcmClientDAO;
import org.sterl.gcm._example.server.client.model.GcmClientBE;
import org.sterl.gcm.api.GcmNotification;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@RequestMapping("/gcmSend")
public class SenderBF {

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
    @Autowired private MessageChannel gcmOutbound;
    @Autowired private GcmClientDAO clientDao;
    
    @PostConstruct
    public void init() {
        clientDao.save(new GcmClientBE("dUXsJW01LzI:APA91bG8x17P9l7uDUvVMKhQ9nZjgasRNE4p8Py86Z3v6C0hlzZva-WDJISWhbrNseWe5zC8IprujZ8cRJB7qrw1vMIg2TWwHUIcCiDOzoccRDbEEZEGP-vCwiGiOBzTS7vTuC6vaBgo"));
    }
    
    /** Sends a normal messages which contains in the data object a text attribute we can read */
    @RequestMapping("/message")
    public SendResult sendSample(@RequestParam(required = false) String message) throws JsonProcessingException {
        if (message == null) message = "Test Message " + new Date();
        
        final SendResult result = new SendResult();
        for (GcmClientBE client : clientDao.findAll()) {
            if (gcmOutbound.send(MessageBuilder.withPayload(message).setHeader(XmppHeaders.TO, client.getId()).build())) {
                result.success();
            } else {
                result.failed();
            }
        }
        return result;
    }
    /** Sends a notification which is displayed by android by default */
    @RequestMapping("/notification")
    public SendResult sendNotification(@RequestParam(required = false) String message) throws JsonProcessingException {
        if (message == null) message = "Test Message " + new Date();
        
        final SendResult result = new SendResult();
        for (GcmClientBE client : clientDao.findAll()) {
            if (gcmOutbound.send(
                    MessageBuilder.withPayload(new GcmNotification(null, message, null))
                        .setHeader(XmppHeaders.TO, client.getId()).build())) {
                result.success();
            } else {
                result.failed();
            }
        }
        return result;
    }
}
