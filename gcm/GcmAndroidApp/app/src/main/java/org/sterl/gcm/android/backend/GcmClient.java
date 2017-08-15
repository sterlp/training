package org.sterl.gcm.android.backend;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GcmClient {

    private String id;

    public GcmClient() {
    }

    public GcmClient(String token) {
        this();
        this.id = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
