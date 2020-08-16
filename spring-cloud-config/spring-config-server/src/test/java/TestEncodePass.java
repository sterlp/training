import static org.junit.jupiter.api.Assertions.*;

import java.security.SecureRandom;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;

class TestEncodePass {

    @Test
    void test() {
        // the server has his instance to create passwords
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(BCryptVersion.$2A, 10, new SecureRandom());
        final String encoded = encoder.encode("pass1");
        // the client can of course have an own instance of his BCryptPasswordEncoder
        assertTrue(new BCryptPasswordEncoder().matches("pass1", encoded));
    }
}
