package org.sterl.training.springquartz.example.jobs;

import java.time.Instant;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.sterl.training.springquartz.example.control.StoreItemService;

import lombok.Setter;

public class RetryJob extends QuartzJobBean {

    @Autowired private StoreItemService storeItemService;
    @Autowired private Scheduler scheduler;
    @Setter
    private int count;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Integer retryCount = getRetryCount(context);
        try {
            storeItemService.createItems(count);
        } catch (Exception e) {
            if (retryCount > 3) {
                throw e;
            } else {
                retryCount = retryCount + 1;
                

                //retryNow(retryCount, context, e);

                try {
                    retryLater(retryCount, context);
                } catch (SchedulerException e1) {
                    e1.printStackTrace();
                    retryNow(retryCount, context,  e);
                }
            }
        }
    }
    // this wil re-schedule the job immediately
    private void retryNow(Integer retryCount, JobExecutionContext context, Exception e) throws JobExecutionException {
        context.getMergedJobDataMap().put("retryCount", retryCount);
        throw new JobExecutionException("Create Items failed  "
                + retryCount + " times. Will retry. " + e.getMessage(),
                true);
    }
    private void retryLater(Integer retryCount, JobExecutionContext context) throws SchedulerException {
        // don't use the getMergedJobDataMap of spring, it will not be updated as we replace it here
        context.getTrigger().getJobDataMap().put("retryCount", retryCount);
        final var b = context.getTrigger().getTriggerBuilder();
        b.startAt(Date.from(Instant.now().plusSeconds(getRetryCount(context) * 5)));
        scheduler.rescheduleJob(context.getTrigger().getKey(), b.build());
    }

    private Integer getRetryCount(JobExecutionContext context) {
        return (Integer)context.getMergedJobDataMap().getOrDefault("retryCount", 1);
    }

}
