package org.sterl.training.springquartz.pmw.boundary;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.sterl.training.springquartz.pmw.model.AbstractWorkflowContext;
import org.sterl.training.springquartz.pmw.model.Step;
import org.sterl.training.springquartz.pmw.model.Workflow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InMemoryWorkflowService {
    private ExecutorService stepExecutor;
    
    public InMemoryWorkflowService() {
        stepExecutor = Executors.newWorkStealingPool();
                // Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);
    }

    public <T extends AbstractWorkflowContext>  void execute(Workflow<T> w, T c) {
        stepExecutor.submit(new StepCallable<T>(w, c));
    }

    @RequiredArgsConstructor
    private class StepCallable<T extends AbstractWorkflowContext> implements Callable<Void> {
        private final Workflow<T> w;
        private final T c;

        @Override
        public Void call() throws Exception {
            Step<T> nextStep = w.getNextStep(c);
            if (nextStep != null) {
                try {
                    nextStep.apply(c);
                    
                    if (w.success(nextStep, c)) {
                        stepExecutor.submit(new StepCallable<T>(w, c));
                    }
                } catch (Exception e) {
                    boolean willRetry = w.fail(nextStep, c, e);
                    if (willRetry && !stepExecutor.isShutdown()) {
                        log.warn("Workflow {} failed. Retry={}", w.getName(), willRetry, e);
                        stepExecutor.submit(new StepCallable<T>(w, c));
                    } else {
                        log.error("Workflow {} failed. Retry={}", w.getName(), willRetry, e);
                    }
                }
            }
            return null;
        } 
    }
    
    public void stop() {
        stepExecutor.shutdown();
    }
}
