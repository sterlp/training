package org.sterl.training.ee.identitystore;

import java.time.Duration;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.sql.DataSource;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.sterl.hash.Algorithm;
import org.sterl.hash.BCryptPbkdf2PasswordHash;
import org.sterl.hash.PasswordHasher;
import org.sterl.identitystore.builder.IdentityStoreBuilder;

@ApplicationScoped
@BasicAuthenticationMechanismDefinition(realmName = "bar")
@DeclareRoles({ "admin", "user" }) // this authorities are allowed
/* // disabled as
@DatabaseIdentityStoreDefinition(
    callerQuery = "select password from users where enabled = true AND username = ?",
    groupsQuery = "select authority from authorities where username = ?",
    dataSourceLookup = "jdbc/identity-store",
    // JEE default
    // hashAlgorithm = Pbkdf2PasswordHash.class
    
    // using custom one which supports bcrypt
    hashAlgorithm = BCryptAndPbkdf2PasswordHash.class
)*/
@ApplicationPath("")
public class ApplicationConfiguration extends Application {
 
    
    @Resource(lookup = "jdbc/identity-store") DataSource dataSource;
    
    @Produces
    @ApplicationScoped
    public org.sterl.identitystore.api.IdentityStore jdbcIdentityStore(PasswordHasher passwordHasher) {
        System.out.println("Build IDentity Store: " + dataSource);
        final org.sterl.identitystore.api.IdentityStore is = IdentityStoreBuilder.jdbcBuilder(dataSource)
                .withCache(Duration.ofMinutes(15))
                .withPasswordHasher(passwordHasher)
                .withCachedPassword(false)
                .withPasswordQuery("select password from users where enabled = true AND username = ?")
                .withGroupsQuery("select authority from authorities where username = ?")
                .build();
        
        return is;
    }
    
    @ApplicationScoped
    @Produces
    public PasswordHasher bCryptAndPbkdf2PasswordHash() {
        return new BCryptPbkdf2PasswordHash(Algorithm.BCrypt);
    }
}
