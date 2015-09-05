package org.sterl;

import lombok.Getter;

public class AbstractTest {

    protected final long CYCLES = 1000000;
    
    public static class TimerMeasure {
        private long measuer = 0;
        /** the overall passed time */
        @Getter
        private long time = 0;
        /** how often start was called */
        @Getter
        private long count = 0;
        
        public void start() {
            ++this.count;
            this.measuer = System.nanoTime();
        }
        public void stop() {
            final long end = System.nanoTime();
            this.time += (end - measuer);
        }
        public long calcAvg() {
            return time / count;
        }
        /** returns the total time measured as milliseconds */
        public long getTimeAsMs() {
            return time / 1000000;
        }
    }
    
    protected TimerMeasure buildMessage = new TimerMeasure();
    protected TimerMeasure serialization = new TimerMeasure();
    protected TimerMeasure deserialization = new TimerMeasure();
   
    protected long messageSize = 0;
    
    protected void writeStats(String forWhat) {
        System.out.println("## " + forWhat);
        System.out.println("* Build Message:   " + buildMessage.getTimeAsMs()     + " ms avg for 1: " + buildMessage.calcAvg() + "ns");
        System.out.println("* Serialization:   " + serialization.getTimeAsMs()    + " ms avg for 1: " + serialization.calcAvg() + "ns");
        System.out.println("* Deserialization: " + deserialization.getTimeAsMs()  + " ms avg for 1: " + deserialization.calcAvg() + "ns");
        System.out.println("* Message Size:    " + messageSize + " bytes");
    }
}
