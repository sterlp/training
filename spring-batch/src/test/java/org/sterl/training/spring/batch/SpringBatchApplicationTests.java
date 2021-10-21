package org.sterl.training.spring.batch;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.sterl.training.spring.batch.JobConfig.ErrorListWriter;

// https://docs.spring.io/spring-batch/docs/current/reference/html/common-patterns.html#commonPatterns
@SpringBootTest
@SpringBatchTest
class SpringBatchApplicationTests {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    ErrorListWriter writer;
    @Test
    void contextLoads() throws Exception {
        // GIVEN
        
        // WHEN
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        
        // THEN
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();
        assertThat(actualJobExitStatus.getExitCode()).isEqualTo(ExitStatus.COMPLETED.getExitCode());
    }
}
