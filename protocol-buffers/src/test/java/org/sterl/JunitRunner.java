package org.sterl;

import org.junit.Test;
import org.sterl.performancetest.TestRunner;

public class JunitRunner {

    @Test
    public void test() throws Exception {
        System.out.println(new TestRunner().runTests());
    }

}
