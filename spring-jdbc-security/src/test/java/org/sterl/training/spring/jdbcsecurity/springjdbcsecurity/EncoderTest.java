package org.sterl.training.spring.jdbcsecurity.springjdbcsecurity;

import org.junit.jupiter.api.Test;
import org.sterl.spring.hash.SpringBCryptPbkdf2PasswordHash;

class EncoderTest {

    @Test
    void test() {
        System.out.println(new SpringBCryptPbkdf2PasswordHash().encode("test"));
        System.out.println(new SpringBCryptPbkdf2PasswordHash().matches("test", 
                "PBKDF2WithHmacSHA256:2048:Muo4cAqMsHCN5d27lQ1IXSEa5mMhwMn6BmubWG7DN9g=:MLJhWHR6Cf7YNYUf3XunIWb+rR2wonhXYBUgZJfVe8M="));
    }

}
