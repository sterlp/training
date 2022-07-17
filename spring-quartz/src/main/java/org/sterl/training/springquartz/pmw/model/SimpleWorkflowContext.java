package org.sterl.training.springquartz.pmw.model;

public class SimpleWorkflowContext extends AbstractWorkflowContext {

    public static final SimpleWorkflowContext newContextFor(Workflow<SimpleWorkflowContext> w) {
        SimpleWorkflowContext result = new SimpleWorkflowContext();
        return result;
    }
}
