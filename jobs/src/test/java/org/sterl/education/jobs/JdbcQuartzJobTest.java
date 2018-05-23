package org.sterl.education.jobs;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.hsqldb.jdbc.JDBCDataSource;
import org.hsqldb.jdbc.JDBCUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.DefaultThreadExecutor;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.jdbcjobstore.InvalidConfigurationException;
import org.quartz.impl.jdbcjobstore.JobStoreTX;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.utils.ConnectionProvider;
import org.quartz.utils.DBConnectionManager;
import org.quartz.utils.PoolingConnectionProvider;
import org.springframework.util.StringUtils;

import lombok.Data;


/**
 * Simple test to show Quartz in Action
 */
public class JdbcQuartzJobTest {
    private final static CountDownLatch COUNT_DOWN = new CountDownLatch(5);
    
    private static final String JOB_NAME = "TEST_JOB_1";
    private static final String JOB_DATA_NAME = "TEST_JOB_DATA_KEY";
    private static JDBCDataSource driver;
    
    @BeforeClass
    public static void beforeClass() throws SQLException, IOException {
        driver = new JDBCDataSource();
        driver.setUrl("jdbc:hsqldb:mem:test");
        driver.setUser("sa");
        
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            String initSQL = IOUtils.toString(
                    JdbcQuartzJobTest.class.getResourceAsStream("/org/quartz/impl/jdbcjobstore/tables_hsqldb.sql"),
                    Charset.forName("UTF-8"));
            statement.execute(initSQL);
            // fix column type in HSQL DB
            statement.execute("ALTER TABLE qrtz_job_details DROP COLUMN JOB_DATA");
            statement.execute("ALTER TABLE qrtz_job_details ADD COLUMN JOB_DATA BLOB");
            
            statement.execute("ALTER TABLE QRTZ_TRIGGERS DROP COLUMN JOB_DATA");
            statement.execute("ALTER TABLE QRTZ_TRIGGERS ADD COLUMN JOB_DATA BLOB");
            connection.commit();
        }
    }
    
    private static Connection getConnection() throws SQLException {
        return driver.getConnection();
    }
    
    /**
     * https://stackoverflow.com/questions/35723948/what-is-the-quartz-default-thread-count?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
     */
    @Test
    public void testQuartz() throws Exception {

        try (TestJdbcScheduler node1 = new TestJdbcScheduler(1, driver);) {
            node1.registerWorker();
            try (Connection connection = getConnection()) {
                JdbcHelper.printQueryResult("SELECT * FROM qrtz_job_details", connection);
            }
            
            for (int i = 0; i < 5; ++i) {
                JobDataMap data = new JobDataMap();
                data.put(JOB_DATA_NAME, "Trigger " + i);
                node1.scheduler.triggerJob(JobKey.jobKey(JOB_NAME), data);
            }
            COUNT_DOWN.await(5, TimeUnit.SECONDS);
        }
    }
    
    @Data
    public static class TestJdbcScheduler implements AutoCloseable {
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
            jdbcJobStore.setInstanceName(name + index);
            jdbcJobStore.setDataSource(DATA_SOURCE_NAME);
            jdbcJobStore.setClusterCheckinInterval(20000L);
            jdbcJobStore.setDbRetryInterval(15000L);
            jdbcJobStore.setIsClustered(true);
            jdbcJobStore.setTablePrefix("qrtz_");
            jdbcJobStore.setDriverDelegateClass("org.quartz.impl.jdbcjobstore.HSQLDBDelegate");
    
            // Create a scheduler.
            DirectSchedulerFactory.getInstance().createScheduler(name, name + index,
                    new SimpleThreadPool(5, Thread.NORM_PRIORITY), jdbcJobStore);
            this.scheduler = DirectSchedulerFactory.getInstance().getScheduler(name);
    
            // Start the scheduler.
            scheduler.start();
        }
        
        public void registerWorker() throws SchedulerException {
            scheduler.addJob(newJob(SimpleJob.class)
                    .withIdentity(JOB_NAME)
                    .usingJobData("index", index)
                    .storeDurably()
                    .build(), true);
        }
        
        @Override
        public void close() throws Exception {
            this.scheduler.shutdown(true);
        }
     }

    /**
     * New instance of this class is created each time ...
     */
    public static class SimpleJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.err.println("Running Job wiht Data: " 
                    + context.getJobDetail().getJobDataMap().get(JOB_DATA_NAME) 
                    + " index: " + context.getJobDetail().getJobDataMap().get("index"));
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {}
            COUNT_DOWN.countDown();
        }
    }
}
