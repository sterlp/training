package org.sterl.training.springquartz.pmw.quartz;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.sterl.training.springquartz.pmw.boundary.QuartzWorkflowService;
import org.sterl.training.springquartz.pmw.component.SimpleWorkflowStepStrategy;
import org.sterl.training.springquartz.pmw.model.Workflow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class PwmQuartzJobFactory implements JobFactory {

    private final SimpleWorkflowStepStrategy strategy;
    private final QuartzWorkflowService workflowService;

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        String name = bundle.getJobDetail().getKey().getName();
        Workflow<?> w = workflowService.getWorkflow(name);
        
        log.debug("{} results in {}", name, w);
        if (w == null) {
            throw new IllegalStateException("No workflow with the name " + name);
        }
        
        return new PmwQuartzJob(strategy, w, scheduler);
    }
}
