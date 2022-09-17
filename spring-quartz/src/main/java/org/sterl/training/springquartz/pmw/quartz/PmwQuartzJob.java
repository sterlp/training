package org.sterl.training.springquartz.pmw.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.sterl.training.springquartz.pmw.component.SimpleWorkflowStepStrategy;
import org.sterl.training.springquartz.pmw.model.AbstractWorkflowContext;
import org.sterl.training.springquartz.pmw.model.SimpleWorkflowContext;
import org.sterl.training.springquartz.pmw.model.Workflow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class PmwQuartzJob implements Job {

    private final SimpleWorkflowStepStrategy callStrategy;
    private final Workflow<? extends AbstractWorkflowContext> w;
    private final Scheduler scheduler;
    

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobData = context.getMergedJobDataMap();
        SimpleWorkflowContext c = new SimpleWorkflowContext();
        c.setNextStep( (Integer)(jobData.getOrDefault("_nextStepId", 0)) );
        
        boolean hasNext = callStrategy.call((Workflow)w, c);
        
        if (hasNext) {
            try {
                queueNextStepFor(context.getTrigger(), c);
            } catch (SchedulerException e) {
                throw new JobExecutionException(e);
            }
        }
    }
    
    void queueNextStepFor(Trigger trigger, SimpleWorkflowContext c) throws SchedulerException {
        Trigger newTrigger = TriggerBuilder.newTrigger()
                .forJob(trigger.getJobKey())
                .usingJobData(trigger.getJobDataMap())
                .usingJobData("_nextStepId", c.getNextStep())
                .startNow()
                .build();
        log.debug("Tiggering step={} workflow={} - oldKey={} newKey={}", 
                w.getName(), c.getNextStep(), trigger.getKey(), newTrigger.getKey());
        scheduler.rescheduleJob(trigger.getKey(), newTrigger);
    }
}
