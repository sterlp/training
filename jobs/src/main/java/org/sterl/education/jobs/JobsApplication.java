package org.sterl.education.jobs;

import static org.quartz.JobBuilder.newJob;

import java.io.IOException;

import javax.sql.DataSource;

import org.postgresql.ds.PGPoolingDataSource;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.impl.jdbcjobstore.InvalidConfigurationException;
import org.quartz.impl.jdbcjobstore.JobStoreTX;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.utils.DBConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;

//@SpringBootApplication
public class JobsApplication {
    private static final String JOB_DATA_NAME = "TEST_JOB_DATA_KEY";
    private static Logger LOG;
    private static int index;

    public static void main(String[] args) throws SchedulerException, InvalidConfigurationException, IOException {
        index = Integer.valueOf(args[0]);
        final int jobCount = Integer.valueOf(args[1]);
        final boolean registerWorker = Boolean.valueOf(args[2]);
        LOG = LoggerFactory.getLogger("NODE_" + index);
        
        final PGPoolingDataSource driver = new PGPoolingDataSource();
        driver.setUrl("jdbc:postgresql://localhost:5432/postgres");
        driver.setUser("postgres");
        driver.setPassword("postgres");
        driver.setMaxConnections(10);
        
        LOG.info("Starting Node {} and will register worker {}", index, registerWorker);
        final TestJdbcScheduler scheduler = new TestJdbcScheduler(index, driver);
        
        if (registerWorker) {
            scheduler.registerWorker();
        }

        if (jobCount > 0) LOG.info("Triggering now {} jobs ...", jobCount);
        for (int i = 1; i <= jobCount; ++i) {
            scheduler.triggerJob("Job Trigger from " + index + " Job " + i);
        }
        System.in.read(); // wait for enter ...
        scheduler.close();
    }
    
    @Data
    public static class TestJdbcScheduler implements AutoCloseable {
        private static final String JOB_NAME = "TEST_JOB_1";

        private static final String DATA_SOURCE_NAME = "DATASOURCE_LOOKUP_NAME";
        private final String name;
        private final Scheduler scheduler;
        private final int index;

        public TestJdbcScheduler(int index, DataSource dataSource) throws SchedulerException, InvalidConfigurationException {
            this.index = index;
            this.name = "TEST-SCHEDULER";
            DBConnectionManager.getInstance().addConnectionProvider(DATA_SOURCE_NAME, 
                    new SimpleConnectionProvider(dataSource)); 
            
            // Configure jdbc store
            JobStoreTX jdbcJobStore = new JobStoreTX();
            jdbcJobStore.setInstanceName(name);
            jdbcJobStore.setDataSource(DATA_SOURCE_NAME);
            jdbcJobStore.setClusterCheckinInterval(20000L);
            jdbcJobStore.setDbRetryInterval(15000L);
            jdbcJobStore.setIsClustered(true);
            jdbcJobStore.setTablePrefix("qrtz_");
            jdbcJobStore.setDriverDelegateClass("org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
    
            // Create a scheduler.
            DirectSchedulerFactory.getInstance().createScheduler(name, name + index,
                    new SimpleThreadPool(1, Thread.NORM_PRIORITY), jdbcJobStore);
            this.scheduler = DirectSchedulerFactory.getInstance().getScheduler(name);
    
            // Start the scheduler.
            scheduler.start();
        }
        
        public void triggerJob(String value) throws SchedulerException {
            JobDataMap data = new JobDataMap();
            data.put(JOB_DATA_NAME, value);
            scheduler.triggerJob(JobKey.jobKey(JOB_NAME), data);
        }
        
        public void registerWorker() throws SchedulerException {
            scheduler.addJob(newJob(SimpleJob.class)
                    .withIdentity(JOB_NAME)
                    .storeDurably()
                    .build(), true);
        }
        public void deleteWorker() throws SchedulerException {
            scheduler.deleteJob(JobKey.jobKey(JOB_NAME));
        }
        
        @Override
        public void close() throws SchedulerException {
            this.scheduler.shutdown(true);
        }
     }

    /**
     * New instance of this class is created each time ...
     */
    public static class SimpleJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap jobDataMap = context.getMergedJobDataMap();
            LOG.info("Running Job with Data: " 
                    + jobDataMap.getString(JOB_DATA_NAME) 
                    + " Node Index: " + index
                    + "... "
                    );
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {}
            LOG.info("... Job finished " + jobDataMap.getString(JOB_DATA_NAME) 
                    + " Node Index: " + index);
        }
    }
}
