package org.sterl.training.ee.identitystore;

import javax.annotation.security.DeclareRoles;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.sterl.jee.hash.BCryptAndPbkdf2PasswordHash;

@ApplicationScoped
@BasicAuthenticationMechanismDefinition(realmName = "bar")
@DeclareRoles({ "admin", "user" }) // this authorities are allowed
@DatabaseIdentityStoreDefinition(
    callerQuery = "select password from users where enabled = true AND username = ?",
    groupsQuery = "select authority from authorities where username = ?",
    dataSourceLookup = "jdbc/identity-store",
    // JEE default
    // hashAlgorithm = Pbkdf2PasswordHash.class
    
    // using custom one which supports bcrypt
    hashAlgorithm = BCryptAndPbkdf2PasswordHash.class
)
@ApplicationPath("")
public class ApplicationConfiguration extends Application {
    
}
