package org.sterl.gcm.api;

public interface GcmXmppHeader {

    /**
     * Parameter for to field.
     */
    public static final String TO = "to";
    
    public static final String MESSAGE_ID = "message_id";

    /**
     * HTTP parameter for registration id.
     */
    public static final String REGISTRATION_ID = "registration_id";

    /**
     * HTTP parameter for collapse key.
     */
    public static final String COLLAPSE_KEY = "collapse_key";

    /**
     * HTTP parameter for delaying the message delivery if the device is idle.
     */
    public static final String DELAY_WHILE_IDLE = "delay_while_idle";

    /**
     * HTTP parameter for telling gcm to validate the message without actually sending it.
     */
    public static final String DRY_RUN = "dry_run";

    /**
     * HTTP parameter for package name that can be used to restrict message delivery by matching
     * against the package name used to generate the registration id.
     */
    public static final String RESTRICTED_PACKAGE_NAME = "restricted_package_name";

    /**
     * Prefix to HTTP parameter used to pass key-values in the message payload.
     */
    public static final String PAYLOAD_PREFIX = "data.";

    /**
     * Parameter used to set the message time-to-live.
     */
    public static final String TIME_TO_LIVE = "time_to_live";

    /**
     * Parameter used to set the message priority.
     */
    public static final String PRIORITY = "priority";

    /**
     * Parameter used to set the content available (iOS only)
     */
    public static final String CONTENT_AVAILABLE = "content_available";
}
