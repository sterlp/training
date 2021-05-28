package org.sterl.training.hystrix;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

@RestController
public class HelloResource {

    @GetMapping("/hello")
    DeferredResult<String> hello() throws Exception {
        DeferredResult<String> result = new DeferredResult<>();

        new HystrixCommand<String>(configHystrixCommand(HelloResource.class.getSimpleName(), "hello", 500)) {
            @Override
            protected String run() throws Exception {
                return Instant.now().toString();
            }
        }
        .toObservable()
        .doOnNext(r -> result.setResult(r))
        .doOnError(e -> result.setErrorResult(e))
        .subscribe();

        return result;
    }
    
    final AtomicInteger errorCounter = new AtomicInteger(0);
    @GetMapping("/withError")
    DeferredResult<String> withError() throws Exception {
        DeferredResult<String> result = new DeferredResult<>();

        new SimpleHystrixCommand<>("withError", errorCounter.incrementAndGet(), 
                (c) -> {
                    if (c.intValue() % 2 == 0) {
                        throw new RuntimeException("rejected " + c);
                    } else {
                        return c + " " + Instant.now().toString() ;
                    }
                })
        .toObservable()
        .doOnNext(r -> result.setResult(r))
        .doOnError(e -> result.setErrorResult(e.getCause()))
        .subscribe();

        return result;
    }
    
    final AtomicInteger timeoutCounter = new AtomicInteger(0);
    @GetMapping("/withTimeout")
    DeferredResult<String> withTimeout() throws Exception {
        DeferredResult<String> result = new DeferredResult<>();

        new SimpleHystrixCommand<>("withTimeout", timeoutCounter.incrementAndGet(), 
                (c) -> {
                    if (c.intValue() % 2 == 0) {
                        try {
                            Thread.sleep(510);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return c + " " + Instant.now().toString() ;
                })
        .toObservable()
        .doOnNext(r -> result.setResult(r))
        .doOnError(e -> result.setErrorResult(e.getCause()))
        .subscribe();

        return result;
    }
    
    /**
     * Simple command just for demonstration.
     */
    static class SimpleHystrixCommand<T, R> extends HystrixCommand<R> {
        final T in;
        final Function<T, R> function;
        
        protected SimpleHystrixCommand(String name,
                T in, Function<T, R> function) {
            super(configHystrixCommand(SimpleHystrixCommand.class.getSimpleName(), name, 100));
            this.in = in;
            this.function = function;
        }

        @Override
        protected R run() throws Exception {
            return this.function.apply(this.in);
        }
    }
    
    private static HystrixCommand.Setter configHystrixCommand(String className, String methodName, int timeoutMs) {
        return HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(className + "Group"))
                .andCommandKey(HystrixCommandKey.Factory.asKey(className + "." + methodName))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(timeoutMs));
    }
}
