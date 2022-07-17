package org.sterl.training.springquartz.pmw.model;

public interface NamedStep<T extends AbstractWorkflowContext> extends Step<T> {
    String getName();
    void apply(T c);
}
