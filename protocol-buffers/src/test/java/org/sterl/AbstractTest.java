package org.sterl;

import java.text.DecimalFormat;

import lombok.Getter;

public class AbstractTest {

    protected final long CYCLES = 1000000;
    
    public static class TimerMeasure {
        final DecimalFormat df = new DecimalFormat("0.00");
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
    
    protected TimerMeasure buildMsg = new TimerMeasure();
    protected TimerMeasure serialis = new TimerMeasure();
    protected TimerMeasure deserial = new TimerMeasure();
   
    protected long messageSize = 0;
    
    protected void writeStats(String forWhat) {
        final String N = "      |  ";
        System.out.println("## " + forWhat);
        System.out.println("| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |");
        System.out.println("|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|");
        System.out.println("| Build Message:   | " + buildMsg.getTimeAsMs() + N + buildMsg.calcAvg() + N + buildMsg.getSlowest() + N + buildMsg.slowPercent() + N + buildMsg.getFastes() + N + buildMsg.fastPercent() + "|");
        System.out.println("| Serialization:   | " + serialis.getTimeAsMs() + N + serialis.calcAvg() + N + serialis.getSlowest() + N + buildMsg.slowPercent() + N + serialis.getFastes() + N + serialis.fastPercent() + "|");
        System.out.println("| Deserialization: | " + deserial.getTimeAsMs() + N + deserial.calcAvg() + N + deserial.getSlowest() + N + buildMsg.slowPercent() + N + deserial.getFastes() + N + deserial.fastPercent() + "|");
        System.out.println("* Message Size: " + messageSize + " bytes");
        System.out.println("* Total time: " + (buildMsg.getTimeAsMs() + serialis.getTimeAsMs() + deserial.getTimeAsMs()) + " ms");
    }
}
