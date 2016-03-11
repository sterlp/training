package org.sterl.gcm.server.msg.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GcmMessage<DataType> {
    private String to;
    @JsonProperty("message_id")
    private String messageId;
    private String priority = "high";
    
    /** Optional notification */
    private GcmNotification notification;
    private DataType data;
    /**
     * his parameter specifies how long (in seconds) the message should be kept in GCM storage if the device is offline. 
     * The maximum time to live supported is 4 weeks, and the default value is 4 weeks. For more information, see Setting the lifespan of a message.
     */
    @JsonProperty("time_to_live")
    private Long ttl;
    @JsonProperty("delay_while_idle")
    private Boolean delayWhileIdle = false;
    @JsonProperty("delivery_receipt_requested")
    private Boolean deliveryReceipt = true;
    
    public GcmMessage(String to, String messageId) {
        super();
        this.to = to;
        this.messageId = messageId;
    }
    public GcmMessage(String to, String messageId, DataType data) {
        this(to, messageId);
        this.data = data;
    }
}
