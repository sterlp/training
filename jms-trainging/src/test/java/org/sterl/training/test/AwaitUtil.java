package org.sterl.training.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.concurrent.Callable;


public class AwaitUtil {

    public static <T> T waitFor(Callable<T> callable, T value) throws Exception {
        T v;
        int count = 0;
        do {
            if (count > 0) Thread.sleep(count * 2);
            v = callable.call();
            ++count;
        } while (!Objects.equals(v, value) && count < 100);
        return v;
    }
    
    public static <T> void assertEquals(Callable<T> callable, T expected) {
        try {
            T v = waitFor(callable, expected);
            assertThat(v).isEqualTo(expected);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
