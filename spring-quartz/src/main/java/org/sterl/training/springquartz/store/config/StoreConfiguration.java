package org.sterl.training.springquartz.store.config;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.sterl.training.springquartz.store.jobs.CreateStoreItemsJob;
import org.sterl.training.springquartz.store.jobs.QuatzTimerJob;

@Configuration
public class StoreConfiguration {

    @Bean
    public JobDetailFactoryBean quatzTimerJob() {
        final var jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(QuatzTimerJob.class);
        jobDetailFactory.setDescription("Simple timer like @Scheduled - but already like with ShedLock");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean
    public JobDetailFactoryBean createStoreItemsJob() {
        final var jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(CreateStoreItemsJob.class);
        jobDetailFactory.setDescription("Job we directly trigger if we want to run it");
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setRequestsRecovery(true);
        return jobDetailFactory;
    }
    
    @Bean
    public SimpleTriggerFactoryBean trigger(JobDetail quatzTimerJob) {
        final var trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(quatzTimerJob);
        trigger.setRepeatInterval(30_000);
        trigger.setPriority(Thread.NORM_PRIORITY - 1);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        return trigger;
    }
}
