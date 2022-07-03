package org.sterl.training.springquartz.quartz.model;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import lombok.Data;

@Data
public class TriggerV1 {
    private TriggerKey key;
    private JobKey jobKey;
    private String calendarName;
    private String description;
    
    private Date startTime;
    private Date endTime;
    private Date nextFireTime;
    
    private int priority;
    private Map<String, Object> jobDataMap;

    public static TriggerV1 of(Trigger trigger) {
        TriggerV1 result = new TriggerV1();
        result.setCalendarName(trigger.getCalendarName());
        result.setDescription(trigger.getDescription());

        result.setStartTime(trigger.getStartTime());
        result.setEndTime(trigger.getEndTime());
        result.setNextFireTime(trigger.getNextFireTime());

        result.setJobKey(trigger.getJobKey());
        result.setKey(trigger.getKey());
        result.setPriority(trigger.getPriority());
        result.setJobDataMap(trigger.getJobDataMap().entrySet().stream()
                .collect(
                        Collectors.toMap(
                                e -> e.getKey(),
                                e -> e.getValue()
                            )
                        )
                );
        return result;
    }
}
