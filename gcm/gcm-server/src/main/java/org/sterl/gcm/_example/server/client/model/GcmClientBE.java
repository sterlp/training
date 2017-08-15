package org.sterl.gcm._example.server.client.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "GCM_CLIENT") @Data @NoArgsConstructor @AllArgsConstructor
public class GcmClientBE {

    /** we just use the token as an ID here */
    @Id
    private String id;
}
