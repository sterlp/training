package org.sterl.training.springquartz.pmw.model;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractWorkflowContext {
    @Getter @Setter
    private Integer lastStep = null;
    @Getter @Setter
    private int nextStep = 0;
}
