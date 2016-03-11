package org.sterl.gcm.server.client;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.sterl.gcm.server.client.model.GcmClientBE;

@RepositoryRestResource(collectionResourceRel = "GcmClient", path = "gcmClient")
public interface GcmClientDAO extends PagingAndSortingRepository<GcmClientBE, String> {

}
