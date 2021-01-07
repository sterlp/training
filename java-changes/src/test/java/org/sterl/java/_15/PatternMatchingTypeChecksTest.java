package org.sterl.java._15;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * https://openjdk.java.net/jeps/378
 */
class PatternMatchingTypeChecksTest {

    @SuppressWarnings("preview")
    @Test
    void test() {
        final Object type = Long.valueOf(4L);
        
        if (type instanceof Long longValue && longValue.longValue() > 3) {
            System.out.println(longValue);
        } else {
            fail();
        }
    }

}
