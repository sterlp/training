package org.sterl.java._14;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import lombok.Data;

/**
 * https://openjdk.java.net/jeps/358
 */
class HelpfulNullPointerExceptionsTest {

    @Data
    final class Foo {
        String name;
    }

    @Test
    void test() {
        var ex = assertThrows(NullPointerException.class, () -> new Foo().getName().toString());
        ex.printStackTrace();
    }

}
