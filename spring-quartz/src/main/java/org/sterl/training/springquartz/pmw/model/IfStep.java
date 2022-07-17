package org.sterl.training.springquartz.pmw.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class IfStep<T extends AbstractWorkflowContext> implements Step<T> {

    private final Function<T, String> decideFunction;
    private final Workflow<T> parent;
    private final Map<String, Step<T>> subSteps = new LinkedHashMap<>();
    
    public IfStep<T> ifSelected(String value, Step<T> step) {
        Step<T> oldStep = subSteps.put(value, step);
        if (oldStep != null) throw new IllegalArgumentException("Step with name " 
                + value + " already exists.");
        return this;
    }

    public Workflow<T> end() {
        return this.parent;
    }

    @Override
    public void apply(T c) {
        String stepName = decideFunction.apply(c);
        Step<T> selectedStep = subSteps.get(stepName);

        if (selectedStep == null) throw new IllegalStateException("No step with name " 
                    + stepName + " exists anymore. Select one of " + subSteps.keySet());

        selectedStep.apply(c);
    }
}
