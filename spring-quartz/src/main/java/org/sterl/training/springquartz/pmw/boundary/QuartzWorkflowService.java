package org.sterl.training.springquartz.pmw.boundary;

import java.util.HashMap;
import java.util.Map;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.sterl.training.springquartz.pmw.component.WorkflowRepository;
import org.sterl.training.springquartz.pmw.model.AbstractWorkflowContext;
import org.sterl.training.springquartz.pmw.model.Workflow;
import org.sterl.training.springquartz.pmw.quartz.PmwQuartzJob;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuartzWorkflowService extends AbstractWorkflowService {

    @NonNull
    private final Scheduler scheduler;
    @NonNull
    private final WorkflowRepository workflowRepository;
    private final Map<String, JobDetail> workflowJobs = new HashMap<>();

    public <T extends AbstractWorkflowContext> void execute(Workflow<T> w, T c) {
        JobDetail job = workflowJobs.get(w.getName());
        if (job == null) throw new IllegalStateException(
                w.getName() + " not registered, register the workflowJobs first.");

        try {
            Trigger t = TriggerBuilder.newTrigger()
                    .forJob(job)
                    .startNow()
                    .build();
            
            scheduler.scheduleJob(t);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends AbstractWorkflowContext> JobDetail register(Workflow<T> w) {
        JobDetail job = JobBuilder.newJob(PmwQuartzJob.class)
                .withIdentity(w.getName(), "pmw")
                .storeDurably()
                .build();

        try {
            workflowRepository.registerUnique(w);
            scheduler.addJob(job, true);
            workflowJobs.put(w.getName(), job);
            return job;
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
    
}
