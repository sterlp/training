package org.sterl.training.spring.batch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.sterl.training.spring.batch.JobConfig.ErrorListWriter;

// https://docs.spring.io/spring-batch/docs/current/reference/html/common-patterns.html#commonPatterns
@SpringBootTest
@SpringBatchTest
class JobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    
    @Autowired
    private List<Job> jobs;
    @Autowired
    private JobLauncher jobLuncher;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    ErrorListWriter writer;

    @Test
    void runJob() throws Exception {
        // GIVEN
        
        // WHEN
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        
        // THEN
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();
        assertThat(actualJobExitStatus.getExitCode()).isEqualTo(ExitStatus.COMPLETED.getExitCode());
    }
    
    @Test
    void runJobByName() throws Exception {
        for (Job job : jobs) {
            System.err.println(job.getName());
        }
        JobExecution execution = jobLuncher.run(jobs.get(0), new JobParameters());
        System.err.println(execution.getJobId());
        System.err.println(execution);
        
        // job already executed once with the given parameters
        assertThrows(JobRestartException.class, () -> 
            jobLuncher.run(jobs.get(0), new JobParameters()));
    }
}
