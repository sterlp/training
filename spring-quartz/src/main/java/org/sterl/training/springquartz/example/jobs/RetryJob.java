package org.sterl.training.springquartz.example.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.sterl.training.springquartz.example.control.StoreItemService;

import lombok.Setter;

public class RetryJob extends QuartzJobBean {

    @Autowired private StoreItemService storeItemService;
    @Setter
    private int count;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Integer retryCount = (Integer)context.getMergedJobDataMap().getOrDefault("retryCount", 1);
        try {
            storeItemService.createItems(count);
        } catch (Exception e) {
            if (retryCount > 3) {
                throw e;
            } else {
                context.getMergedJobDataMap().put("retryCount", retryCount + 1);
                throw new JobExecutionException("Create Items failed  " 
                        + retryCount + " times. Will retry. " + e.getMessage(), 
                        true);
            }
        }
    }
}
