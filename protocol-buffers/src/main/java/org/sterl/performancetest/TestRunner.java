package org.sterl.performancetest;

import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    private final int CYCLES = 10000000;
    private List<AbstractTest> tests = new ArrayList<>();
    
    public TestRunner()  {
        tests.add(new SerializationJacksonTest(CYCLES));
        tests.add(new SerializationProtoTest(CYCLES));
        tests.add(new SerializationWireTest(CYCLES));
    }
    
    public StringBuilder runTests() throws Exception {
        StringBuilder result = new StringBuilder();
        for (AbstractTest test : tests) {
            System.gc(); // clean for the test
            test.doTest(result);
        }
        return result;
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println( new TestRunner().runTests() );
    }
}
