package org.sterl.training.springquartz.pmw.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.sterl.training.springquartz.pmw.model.Workflow;

public class WorkflowRepository {

    private final Map<String, Workflow<?>> workflows = new HashMap<>();
    
    public Workflow<?> register(Workflow<?> w) {
        return workflows.put(w.getName(), w);
    }
    
    public void registerUnique(Workflow<?> w) {
        Workflow<?> oldWorkflow = register(w);
        if (oldWorkflow != null) {
            throw new IllegalArgumentException("Workflow with the name " 
                    + w.getName() + " already registered.");
        } 
    }
    public Optional<Workflow<?>> findWorkflow(String name) {
        Workflow<?> w = workflows.get(name);
        return w == null ? Optional.empty() : Optional.of(w);
    }
    public Workflow<?> getWorkflow(String name) {
        Workflow<?> w = workflows.get(name);
        if (w == null) {
            throw new IllegalStateException("No workflow with the name " 
                    + name + " found. Registered " + workflows.keySet());
        }
        return w;
    }
}
