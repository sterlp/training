package org.sterl.training.springquartz.pmw.model;

public interface Step<T extends AbstractWorkflowContext> {
    void apply(T c);
}
