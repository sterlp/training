package org.sterl.training.springquartz.store.api;

import java.util.Date;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/store-jobs")
public class StoreJobController {
    
    @Autowired Scheduler scheduler;
    @Autowired JobDetail createStoreItemsJob;

    @RequiredArgsConstructor
    @Getter
    class TriggerResult {
        final TriggerState state;
        final String jobId;
        Date start;
        Date end;
        public TriggerResult with(Trigger t) {
            if (t != null) {
                start = t.getStartTime();
                end = t.getEndTime();
            }
            return this;
        }
    }
    @GetMapping("/{jobId}")
    @Transactional
    public TriggerResult get(@PathVariable String jobId) throws SchedulerException {
        TriggerState s = scheduler.getTriggerState(new TriggerKey(jobId));
        Trigger t = scheduler.getTrigger(new TriggerKey(jobId));
        return new TriggerResult(s, jobId).with(t);
    }
    
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @PostMapping
    @Transactional
    public Object triggerStoreItemCreation(@RequestBody long count) throws SchedulerException {
        Trigger t = TriggerBuilder.newTrigger()
            .forJob(createStoreItemsJob)
            .startNow()
            .usingJobData("count", count)
            .build();
        
        scheduler.scheduleJob(t);
        log.info("Triggered {} with count {}", t.getKey(), count);
        return t.getKey();
    }
}
