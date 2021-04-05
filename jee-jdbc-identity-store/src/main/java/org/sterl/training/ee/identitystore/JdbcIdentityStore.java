package org.sterl.training.ee.identitystore;

import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.security.enterprise.credential.BasicAuthenticationCredential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import org.sterl.identitystore.api.VerificationResult;

@ApplicationScoped
public class JdbcIdentityStore implements IdentityStore {

    @Inject org.sterl.identitystore.api.IdentityStore is;
    
    public CredentialValidationResult validate(BasicAuthenticationCredential credential) {
        return validate(credential.getCaller(),  credential.getPasswordAsString());
    }

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        return validate(credential.getCaller(),  credential.getPasswordAsString());
    }
    
    private CredentialValidationResult validate(String user, String password) {
        try {
            // if (1 == 1) throw new RuntimeException("Not today!");

            final VerificationResult vr = is.verify(user, password);
            if (vr.getStatus() == VerificationResult.Status.VALID) {
                return new CredentialValidationResult(user, vr.getGroups());
            } else {
                return CredentialValidationResult.INVALID_RESULT;
            }
        } catch (Exception e) {
            // NOTE: with payara 4 manuall logging is needed
            // this is fixed in newer payara 5 versions, otherwise this exception is lost
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "Login failed", e);
            throw e;
        }
    } 
}
