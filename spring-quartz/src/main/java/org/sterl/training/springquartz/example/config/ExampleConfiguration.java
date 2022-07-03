package org.sterl.training.springquartz.example.config;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.sterl.training.springquartz.example.jobs.NotifyUserJob;
import org.sterl.training.springquartz.example.jobs.QuatzTimerJob;
import org.sterl.training.springquartz.example.jobs.RetryJob;
import org.sterl.training.springquartz.example.jobs.SleepJob;

@Configuration
public class ExampleConfiguration {

    @Bean
    public JobDetailFactoryBean quatzTimerJob() {
        final var jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(QuatzTimerJob.class);
        jobDetailFactory.setName("TIMER-JOB");
        jobDetailFactory.setDescription("Simple timer like @Scheduled - but already like with ShedLock");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }
    @Bean
    public SimpleTriggerFactoryBean timerTrigger(JobDetail quatzTimerJob) {
        final var trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(quatzTimerJob);
        trigger.setRepeatInterval(30_000);
        trigger.setName("QUARTZ-TIMER-JOB-TRIGGER");
        trigger.setPriority(Thread.NORM_PRIORITY - 1);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        return trigger;
    }

    @Bean
    public JobDetailFactoryBean retryJob() {
        final var jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(RetryJob.class);
        jobDetailFactory.setName("RETRY-JOB");
        jobDetailFactory.setDescription("Job we directly trigger if we want to run it");
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setRequestsRecovery(true);
        return jobDetailFactory;
    }

    @Bean
    public JobDetailFactoryBean sleepJob() {
        final var jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(SleepJob.class);
        jobDetailFactory.setName("SLEEP-JOB");
        jobDetailFactory.setDescription("Simple jobs which sleeps the given seconds");
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setRequestsRecovery(true);
        return jobDetailFactory;
    }

    @Bean
    public JobDetailFactoryBean notifyUserJob() {
        final var jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(NotifyUserJob.class);
        jobDetailFactory.setName(NotifyUserJob.ID);
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setRequestsRecovery(true);
        return jobDetailFactory;
    }
}
