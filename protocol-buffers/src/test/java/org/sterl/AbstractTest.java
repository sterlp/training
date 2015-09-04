package org.sterl;

public class AbstractTest {

    protected final long CYCLES = 1000000;
    
    protected void writeStats(long buildMessage, long serialization, long deserialization, long messageSize) {
        System.out.println("buildMessage:     " + buildMessage / 1000000      + " ms avg for 1:  " + buildMessage / CYCLES + "ns");
        System.out.println("serialization:   " + serialization / 1000000     + " ms avg for 1: " + serialization / CYCLES + "ns");
        System.out.println("deserialization: " + deserialization / 1000000   + " ms avg for 1: " + deserialization / CYCLES + "ns");
        System.out.println("messageSize:      " + messageSize + " bytes");
    }
}
