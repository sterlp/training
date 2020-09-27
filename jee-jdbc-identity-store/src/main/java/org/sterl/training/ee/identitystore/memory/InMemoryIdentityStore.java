package org.sterl.training.ee.identitystore.memory;

import java.util.Arrays;
import java.util.HashSet;
import javax.security.enterprise.credential.BasicAuthenticationCredential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

// Disabled, just as an example if a custom store should be implemented
// @ApplicationScoped
public class InMemoryIdentityStore implements IdentityStore {

    public CredentialValidationResult validate(BasicAuthenticationCredential credential) {
        System.out.println("BasicAuthenticationCredential: " + credential.getCaller());
        return new CredentialValidationResult(credential.getCaller(), new HashSet<>(Arrays.asList("admin")));
    }
    
    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        System.out.println("UsernamePasswordCredential: " + credential.getCaller());
        return new CredentialValidationResult(credential.getCaller(), new HashSet<>(Arrays.asList("admin")));
    }
}
