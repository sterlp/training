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