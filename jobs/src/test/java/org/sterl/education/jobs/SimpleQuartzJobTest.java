package org.sterl.education.jobs;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;


/**
 * Simple test to show Quartz in Action
 */
public class SimpleQuartzJobTest {
    
    final static CountDownLatch countDown = new CountDownLatch(2);
    
    @Test
    public void testQuartz() throws Exception {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        JobDetail job = newJob(SimpleJob.class)
                .withIdentity("job1", "group1")
                .build();

            CronTrigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(cronSchedule("* * * * * ?"))
                .build();

        sched.scheduleJob(job, trigger);
        sched.start(); // start our scheduler -- otherwise nothing happens

        countDown.await(5l, TimeUnit.SECONDS);

        sched.shutdown(true); // stop our scheduler -- again
    }
    /**
     * New instance of this class is created each time ...
     */
    public static class SimpleJob implements Job {
        private static int count = 1;
        private int internalCount = 1;
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.err.println("Job executed " + count++ + " time(s) - internal field count: " + internalCount++);
            countDown.countDown();
        }
    }
}
