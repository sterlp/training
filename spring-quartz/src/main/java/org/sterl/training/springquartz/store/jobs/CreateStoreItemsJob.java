package org.sterl.training.springquartz.store.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.sterl.training.springquartz.store.control.StoreService;

public class CreateStoreItemsJob implements Job {

    @Autowired StoreService storeService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.err.println("Triggered -> " + storeService + " " + context.getMergedJobDataMap().getLong("count"));
        try {
            Thread.sleep(30_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.err.println("finished -> " + storeService);
    }

}
