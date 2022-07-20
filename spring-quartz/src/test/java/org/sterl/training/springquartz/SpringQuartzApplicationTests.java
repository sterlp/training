package org.sterl.training.springquartz;

import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.sterl.training.springquartz.pmw.boundary.QuartzWorkflowService;
import org.sterl.training.springquartz.pmw.model.SimpleWorkflowContext;
import org.sterl.training.springquartz.pmw.model.Workflow;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class SpringQuartzApplicationTests {

    @Autowired
    QuartzWorkflowService subject;

    @Test
    void testSimpleWorkflow() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        Workflow<SimpleWorkflowContext> w = new Workflow<SimpleWorkflowContext>("test-workflow")
            .next(c -> {
                log.info("hallo 1 {}", c.getNextStep());
            })
            .next(c -> {
                log.info("hallo 2 {}", c.getNextStep());
            })
            .choose(c -> {
                    log.info("choose");
                    return "left";
                }).ifSelected("left", c -> {
                    log.info("  going left");
                }).ifSelected("right", c -> {
                    log.info("  going right");
                })
            .end()
            .next(c -> {
                log.info("finally");
                latch.countDown();
            });
        
        subject.register(w);
        subject.execute(w, SimpleWorkflowContext.newContextFor(w));
        
        latch.await();
    }

}
