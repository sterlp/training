package org.sterl.training.springquartz.pmw.service;

import java.util.Optional;

import org.sterl.training.springquartz.pmw.model.AbstractWorkflowContext;
import org.sterl.training.springquartz.pmw.model.Step;
import org.sterl.training.springquartz.pmw.model.Workflow;

public class WorkflowService {

    public <T extends AbstractWorkflowContext>  void execute(Workflow<T> w, T c) {
        
        executeNextStep(w, c);
    }
    
    <T extends AbstractWorkflowContext> void executeNextStep(
            Workflow<T> w, T c) {
        
        Step<T> nextStep = w.getNextStep(c);
        try {
            nextStep.apply(c);
            
            // TODO
            if (w.success(nextStep, c)) executeNextStep(w, c);

        } catch (Exception e) {
            w.fail(nextStep, c, e);
            e.printStackTrace();
        }
    }
}
