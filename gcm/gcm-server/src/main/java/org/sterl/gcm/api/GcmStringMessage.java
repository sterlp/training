package org.sterl.gcm.api;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The data object in the GCM message needs to be a "complex" JSON object as so we have to wrap
 * simple strings to messages ... .
 */
@Data @AllArgsConstructor
public class GcmStringMessage {
    private final String text;
}
