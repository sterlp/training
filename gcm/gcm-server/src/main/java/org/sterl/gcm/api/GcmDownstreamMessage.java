/**
 * Copyright 2016 Paul Sterl
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific 
 * language governing permissions and limitations under the License.
 */
package org.sterl.gcm.api;

import java.util.Set;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * https://developers.google.com/cloud-messaging/downstream
 * https://developers.google.com/cloud-messaging/http-server-ref#downstream-http-messages-json
 * 
 * @param <DataType> the generic data object which should be send to the phone / device / receiver
 */
@Data @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GcmDownstreamMessage<DataType> {
    /**
     * This parameter specifies the recipient of a message.
     * The value must be a registration token, notification key, or topic.
     */
    @NotNull
    protected String to;
    /**
     * This parameter specifies a list of devices (registration tokens, or IDs) receiving a multicast message. It must contain at least 1 and at most 1000 registration tokens.
     * Use this parameter only for multicast messaging, not for single recipients. Multicast messages (sending to more than 1 registration tokens) are allowed using HTTP JSON format only.
     */
    @JsonProperty("registration_ids")
    protected Set<String> registrationIds;
    /** Unique ID of this message used to map to the ack and nack on the inbound channel */
    @JsonProperty("message_id")
    @NotNull
    protected String messageId;
    
    /**
     * This parameter identifies a group of messages (e.g., with collapse_key: "Updates Available") that can be collapsed, 
     * so that only the last message gets sent when delivery can be resumed. 
     * This is intended to avoid sending too many of the same messages when 
     * the device comes back online or becomes active (see delay_while_idle).
     * 
     * Note that there is no guarantee of the order in which messages get sent.
     * 
     * Note: A maximum of 4 different collapse keys is allowed at any given time. 
     * This means a GCM connection server can simultaneously store 4 different send-to-sync messages per client app. 
     * If you exceed this number, there is no guarantee which 4 collapse keys the GCM connection server will keep.
     */
    @JsonProperty("collapse_key")
    protected String collapseKey;
    /**
     * When this parameter is set to true, it indicates that the message should not be sent until the device becomes active.
     * The default value is false.
     */
    @JsonProperty("delay_while_idle")
    protected Boolean delayWhileIdle = false;
    
    protected String priority = "high";
    
    /** 
     * Optional notification, which is automatically displayed to the user
     * <b>Note: Always collapsible also if it contains data.</b>
     */
    protected GcmNotification notification;
    /**
     * This parameter specifies the custom key-value pairs of the message's payload.
     * 
     * For example, with data:{"score":"3x1"}:
     * 
     * On Android, this would result in an intent extra named score with the string value 3x1.
     * 
     * On iOS, if the message is sent via APNS, it represents the custom data fields. 
     * If it is sent via GCM connection server, it would be represented as key value dictionary in AppDelegate application:didReceiveRemoteNotification:
     * 
     *  The key should not be a reserved word ("from" or any word starting with "google" or "gcm"). Do not use any of the words defined in this table (such as collapse_key).
     *  
     *  Values in string types are recommended. You have to convert values in objects or other non-string data types (e.g., integers or booleans) to string.
     */
    protected DataType data;
    /**
     * his parameter specifies how long (in seconds) the message should be kept in GCM storage if the device is offline. 
     * The maximum time to live supported is 4 weeks, and the default value is 4 weeks. For more information, see Setting the lifespan of a message.
     */
    @JsonProperty("time_to_live")
    protected Long ttl;
    
    @JsonProperty("delivery_receipt_requested")
    protected Boolean deliveryReceipt = true;
    
    public GcmDownstreamMessage(String to, String messageId) {
        super();
        this.to = to;
        this.messageId = messageId;
    }
    public GcmDownstreamMessage(String to, String messageId, DataType data) {
        this(to, messageId);
        this.data = data;
    }
}
