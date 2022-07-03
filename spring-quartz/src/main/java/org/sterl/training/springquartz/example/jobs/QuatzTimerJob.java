package org.sterl.training.springquartz.example.jobs;

import java.time.Instant;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.sterl.training.springquartz.example.control.StoreItemService;

import lombok.extern.slf4j.Slf4j;

// runs only one of these jobs in out cluster
// note we have no spring annotation here, but it contains spring beans
@Slf4j
@DisallowConcurrentExecution
public class QuatzTimerJob implements Job {

    @Autowired StoreItemService storeItemService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Timer fired (has spring bean: {}) - {}",
                storeItemService != null,
                Instant.now());

    }

}
