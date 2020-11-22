package org.sterl.training.ee.identitystore.memory;

import java.util.Arrays;
import java.util.HashSet;
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
        final VerificationResult vr = is.verify(user, password);
        if (vr.getStatus() == VerificationResult.Status.VALID) {
            return new CredentialValidationResult(user, vr.getGroups());
        } else {
            return CredentialValidationResult.INVALID_RESULT;
        }
        
    } 
}
