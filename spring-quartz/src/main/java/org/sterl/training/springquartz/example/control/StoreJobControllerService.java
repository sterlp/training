package org.sterl.training.springquartz.example.control;

import java.time.Instant;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sterl.training.springquartz.example.jobs.NotifyUserJob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(timeout = 10)
@Slf4j
@RequiredArgsConstructor
public class StoreJobControllerService {
    
    private final Scheduler scheduler;
    private final JobDetail retryJob;
    private final JobDetail sleepJob;
    
    public TriggerKey notifyUser(String user) throws SchedulerException {
        TriggerKey key = new TriggerKey(user, NotifyUserJob.ID);
        var t = TriggerBuilder.newTrigger()
                .forJob(JobKey.jobKey(NotifyUserJob.ID))
                // in 7 days - here seconds
                .startAt(Date.from(Instant.now().plusMillis(7_000)))
                .usingJobData("user", user)
                .withIdentity(key)
                .build();
        scheduler.scheduleJob(t);
        return t.getKey();
    }
    
    public TriggerKey createItems(int count) throws SchedulerException {
        var t = TriggerBuilder.newTrigger()
                .forJob(retryJob)
                .startNow()
                .usingJobData("count", count)
                .build();
        scheduler.scheduleJob(t);
        return t.getKey();
    }

    public TriggerKey createStoreSleepJob(int sleepInS, String id, String group) throws SchedulerException {
        var builder = TriggerBuilder.newTrigger()
                .forJob(sleepJob)
                .startNow()
                .usingJobData("sleepInS", sleepInS);
        
        if (id != null || group != null) {
            builder.withIdentity(id, group);
        }
        var t = builder.build();
            
        scheduler.scheduleJob(t);
        log.info("Triggered sleep job {} with time {}", t.getKey(), sleepInS);
        return t.getKey();
    }

}
