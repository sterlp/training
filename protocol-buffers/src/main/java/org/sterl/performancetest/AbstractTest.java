package org.sterl.performancetest;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import lombok.Getter;

public abstract class AbstractTest<MessageType> {

    public static class TimerMeasure {
        final DecimalFormat df = new DecimalFormat("0.0000");
        private long measuer = 0;
        /** the overall passed time */
        @Getter
        private long time = 0;
        /** how often start was called */
        @Getter
        private long count = 0;
        
        @Getter
        private long slowest = 0;
        private long slowestCount = 0;
        
        @Getter
        private long fastes = Long.MAX_VALUE;
        private long fastesCount = 0;
        
        public void start() {
            ++this.count;
            this.measuer = System.nanoTime();
        }
        public void stop() {
            final long end = System.nanoTime();
            final long time = end - measuer; 
            this.time += time;
            // get the fastest stats
            if (time < fastes) {
                fastes = time;
                fastesCount = 1; // reset the counter
            } else if (time == fastes) ++fastesCount;
            
            if (time > slowest) {
                slowest = time;
                slowestCount = 1;
            } else if (time == slowest) ++slowest;
        }
        public long calcAvg() {
            return time / count;
        }
        /** returns the total time measured as milliseconds */
        public long getTimeAsMs() {
            return time / 1000000;
        }
        
        public String slowPercent() {
            if (slowestCount > 0) {
                return df.format((double)((double)slowestCount / (double)count) * 100);
            } else return "--";
        }
        
        public String fastPercent() {
            if (fastesCount > 0) {
                return df.format((double)((double)fastesCount / (double)count) * 100);
            } else return "--";
        }
    }
    
    private TimerMeasure buildMsg = new TimerMeasure();
    private TimerMeasure serialis = new TimerMeasure();
    private TimerMeasure deserial = new TimerMeasure();
   
    private long messageSize = 0;
    
    private Random r;
    protected int[] newRandomNumbers() {
        return new int[] {
            r.nextInt(65000),
            r.nextInt(65000),
            r.nextInt(65000),
            r.nextInt(65000),
            r.nextInt(65000)
        };
    }
    protected String[] newRandomStrings() {
        return new String[] {
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        };
    }
    
    /** 5 random numbers for each cycle */
    protected int[] numbers;
    /** 5 random Strings for each cycle */
    protected String[] strings;
    
    private int cycles = 1000000;
    public AbstractTest(int cycles) {
        this.cycles = cycles;
        r = new Random(cycles * System.currentTimeMillis());
    }
    
    public void doTest(StringBuilder result) throws Exception {
        
        for (int i = 0; i < cycles; i++) {
            numbers = newRandomNumbers();
            strings = newRandomStrings();

            beforeTest();
            
            buildMsg.start();
            MessageType msg = this.buildMessage();
            buildMsg.stop();
            
            serialis.start();
            byte[] bytes = this.serialization(msg);
            serialis.stop();
            messageSize = Math.max(messageSize, bytes.length); // take the max here
            
            deserial.start();
            MessageType buildObject = this.deserialization(bytes);
            deserial.stop();
            
            if (!msg.equals(buildObject)) {
                throw new IllegalStateException("Deserialization failed, message no longer equal!");
            }
            if (!Arrays.equals(bytes, this.serialization(buildObject))) {
                throw new IllegalStateException("Byte message not equals anymore after second serialization!");
            }
        }
        writeStats(result);
    }
    /** Init code */
    protected abstract void beforeTest();
    /** 
     * Build the message
     * @return the build message
     */
    protected abstract MessageType buildMessage()  throws Exception ;
    protected abstract byte[] serialization(MessageType msg) throws Exception ;
    protected abstract MessageType deserialization(byte[] data) throws Exception ;
    
    protected abstract String getName();
    
    protected void writeStats(StringBuilder result) {
        final String N = "        |      ";
        final char L = '\n';
        result.append(L).append("## " + getName());
        result.append(L).append("| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |");
        result.append(L).append("|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|");
        result.append(L).append("| Build Message:   | " + buildMsg.getTimeAsMs() + N + buildMsg.calcAvg() + N + buildMsg.getSlowest() + N + buildMsg.slowPercent() + N + buildMsg.getFastes() + N + buildMsg.fastPercent() + "|");
        result.append(L).append("| Serialization:   | " + serialis.getTimeAsMs() + N + serialis.calcAvg() + N + serialis.getSlowest() + N + buildMsg.slowPercent() + N + serialis.getFastes() + N + serialis.fastPercent() + "|");
        result.append(L).append("| Deserialization: | " + deserial.getTimeAsMs() + N + deserial.calcAvg() + N + deserial.getSlowest() + N + buildMsg.slowPercent() + N + deserial.getFastes() + N + deserial.fastPercent() + "|");
        result.append(L).append("* Message Size: " + messageSize + " bytes");
        result.append(L).append("* Total time: " + (buildMsg.getTimeAsMs() + serialis.getTimeAsMs() + deserial.getTimeAsMs()) + " ms");
        result.append(L);
    }
}
