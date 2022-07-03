package org.sterl.training.springquartz.quartz.api;

import java.util.Collection;
import java.util.Set;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.utils.Key;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sterl.training.springquartz.quartz.model.TriggerV1;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/quartz")
@RequiredArgsConstructor
public class QuartzController {

    private final Scheduler scheduler;

    @GetMapping("/jobs")
    public Collection<JobKey> getJobs() throws SchedulerException {
        return scheduler.getJobKeys(GroupMatcher.anyJobGroup());
    }
    @GetMapping("/triggers")
    public Set<TriggerKey> getTriggers() throws SchedulerException {
        return scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
    }
    @GetMapping("/triggers/{name}")
    public TriggerV1 getTrigger(@PathVariable String name) throws SchedulerException {
        return this.getTrigger(name, Key.DEFAULT_GROUP);
    }
    @GetMapping("/triggers/{name}/{group}")
    public TriggerV1 getTrigger(@PathVariable String name, 
            @PathVariable String group) throws SchedulerException {
        return TriggerV1.of(scheduler.getTrigger(new TriggerKey(name, group)));
    }
}
