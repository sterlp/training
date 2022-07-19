package org.sterl.training.springquartz.pmw.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractWorkflowContext {
    private int nextStep = 0;
    private Exception lastError;
    private Integer lastFailedStep;
    private int retryCount = 0;
    
    public int retry(Exception e) {
        this.lastError = e;
        return this.retryCount++;
    }
}
