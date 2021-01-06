package org.sterl.java._15;

import org.junit.jupiter.api.Test;

/**
 * https://openjdk.java.net/jeps/378
 */
class TextBlocksTest {

    
    @Test
    void test() {
        final String textBlockString = """
            Hello cool
                text block <b>including</b> also HTML \"tags\".
            """;
        
        System.out.println(textBlockString);
    }

}
