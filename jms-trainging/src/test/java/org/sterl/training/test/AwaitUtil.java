package org.sterl.training.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Callable;


public class AwaitUtil {
    public static <T> T waitFor(Callable<T> callable, T value, Duration duration) throws Exception {
        T v;
        int count = 0;
        final long startTime = System.currentTimeMillis();
        do {
            // wait some time, if we are in retry
            if (count > 0) Thread.sleep( Math.min(count * 2, deltaToNow(duration, startTime)) );
            v = callable.call();
            ++count;
        } while (!Objects.equals(v, value) && deltaToNow(duration, startTime) > 0);
        return v;
    }

    public static <T> void assertEquals(Callable<T> callable, T expected) {
        AwaitUtil.assertEquals(callable, expected, Duration.ofSeconds(5));
    }
    
    public static <T> void assertEquals(Callable<T> callable, T expected, Duration duration) {
        try {
            T v = waitFor(callable, expected, duration);
            assertThat(v).isEqualTo(expected);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private static final long deltaToNow(Duration duration, long startTime) {
        return duration.toMillis() - (System.currentTimeMillis() - startTime);
    }
}
