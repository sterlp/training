package org.sterl.training.springquartz.example.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

// spell issue, without ID a fix to the name would keep both jobs in the quartz tables
public class NotifyUserJob implements Job {
    public static final String ID = "NOTIFY-USER-JOB";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.err.println("Notify E-Mail to " +
                context.getMergedJobDataMap().getString("user"));

    }
}
