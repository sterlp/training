package org.sterl.training.ee.identitystore;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.sterl.jee.hash.BCryptAndPbkdf2PasswordHash;

@Path("")
@ApplicationScoped
public class SampleResource {
    @Inject BCryptAndPbkdf2PasswordHash passwordHasher;
    @PersistenceContext private EntityManager em;
    
    @RolesAllowed("user")
    @GET
    @Transactional
    public List<SampleEntity> get() {
        return em.createQuery("FROM SampleEntity e", SampleEntity.class).getResultList();
    }
    
    @RolesAllowed("admin")
    @DELETE @Path("{id}")
    @Transactional
    public int delete(@PathParam("id") long id) {
        return em.createQuery("DELETE FROM SampleEntity e WHERE e.id = :id")
                 .setParameter("id", id).executeUpdate();
    }
    
    @Path("/hash")
    @POST
    public String hash(String password) {
        return passwordHasher.generate(password.toCharArray());
    }
}
