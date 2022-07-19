package org.sterl.training.springquartz.pmw.component;

import org.sterl.training.springquartz.pmw.model.AbstractWorkflowContext;
import org.sterl.training.springquartz.pmw.model.Step;
import org.sterl.training.springquartz.pmw.model.Workflow;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleWorkflowStepStrategy {

    /**
     * Runs the next step in the workflow
     * @return <code>true</code> if a retry or next step should run, otherwise <code>false</code>
     */
    public <T extends AbstractWorkflowContext> boolean call(Workflow<T> w, T c) {
        Step<T> nextStep = w.getNextStep(c);
        if (nextStep != null) {
            try {
                nextStep.apply(c);
                
                return w.success(nextStep, c);

            } catch (Exception e) {
                boolean willRetry = w.fail(nextStep, c, e);
                if (willRetry) {
                    log.warn("Workflow {} failed. Retry={}", w.getName(), willRetry, e);
                } else {
                    log.error("Workflow {} failed. Retry={}", w.getName(), willRetry, e);
                }
                return willRetry;
            }
        }
        return false;
    }
}
