package org.sterl.training.spring.batch;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
public class JobConfig {

    @Autowired
    public JobBuilderFactory jobFactory;
    @Autowired
    public StepBuilderFactory stepFactory;
    
    @Bean
    public ErrorListWriter writer() {
        return new ErrorListWriter();
    }
    @Bean
    public Step testStep() {
        return stepFactory.get("testStep")
            .<String, String>chunk(1)
            .reader(new ListItemReader<>(Collections.synchronizedList(Arrays.asList("1", "2", "3", "4"))))
            .writer(writer())
            //.faultTolerant()
            //.retryLimit(3)
            //.retry(RuntimeException.class)
            .build();
    }
    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
    @Bean
    public Job testJob() {
        return jobFactory.get("testJob")
            .incrementer(new RunIdIncrementer())
            .preventRestart() // optional make sure each job is executed just once
            .flow(testStep())
            .end()
            .build();
    }
    
    public static class ErrorListWriter implements ItemWriter<String> {
        volatile int calls = 0;
        @Override
        public void write(List<? extends String> items) {
            ++calls;
            if (calls % 2 == 0) {
                System.out.println("ErrorListWriter SUCCESS: " + items);
            } else {
                System.out.println("ErrorListWriter ERROR:   " + items);
                throw new RuntimeException("Not now!");
            }
        }
    }
}
