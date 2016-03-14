package org.sterl.gcm.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An GCM message can contain a notification payload in addition to the data payload.
 * 
 * https://developers.google.com/cloud-messaging/http-server-ref#notification-payload-support
 * https://developers.google.com/cloud-messaging/concept-options#notifications_and_data_messages
 */
@Data @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GcmNotification {
    /** Indicates notification title. This field is not visible on iOS phones and tablets. If <code>null</code> android will use the app name */
    protected String title;
    /** Indicates notification body text. */
    protected String body;
    /**
     * Indicates notification icon. On Android: sets value to myicon for drawable resource myicon.
     */
    protected String icon;
}