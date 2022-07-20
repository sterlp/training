package org.sterl.training.springquartz.pmw.quartz;

import java.util.Optional;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.sterl.training.springquartz.pmw.component.SimpleWorkflowStepStrategy;
import org.sterl.training.springquartz.pmw.component.WorkflowRepository;
import org.sterl.training.springquartz.pmw.model.Workflow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class PwmQuartzJobFactory implements JobFactory {

    private final SimpleWorkflowStepStrategy strategy;
    private final WorkflowRepository workflowRepository;
    private final JobFactory delegate;

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        String name = bundle.getJobDetail().getKey().getName();
        Optional<Workflow<?>> w = workflowRepository.findWorkflow(name);
        
        log.debug("{} results in {}", name, w);
        if (w.isEmpty() && delegate == null) {
            throw new IllegalStateException("No workflow with the name " + name);
        } else if (w.isEmpty() && delegate != null) {
            return delegate.newJob(bundle, scheduler);
        }
        
        return new PmwQuartzJob(strategy, w.get(), scheduler);
    }
}
