package org.sterl.training.springquartz.example.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.sterl.training.springquartz.example.control.StoreItemService;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SleepJob extends QuartzJobBean {

    @Autowired StoreItemService storeItemService;

    @Setter
    private int sleepInS;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("executeInternal sleep time {}", sleepInS);
        try {
            Thread.sleep(sleepInS * 1000);
        } catch (InterruptedException e) {}
        log.info("finished {} ", storeItemService);
        
    }

}
