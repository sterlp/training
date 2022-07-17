package org.sterl.training.springquartz.pmw;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sterl.training.springquartz.pmw.model.SimpleWorkflowContext;
import org.sterl.training.springquartz.pmw.model.Workflow;
import org.sterl.training.springquartz.pmw.service.WorkflowService;

class ExampleWorkflowTest {
    
    WorkflowService workflowService = new WorkflowService();

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void test() {
        assertThat(workflowService).isNotNull();
    }
    
    @Test
    void testWorkflow() {
        Workflow<SimpleWorkflowContext> w = new Workflow<SimpleWorkflowContext>("test-workflow")
            .next(c -> {
                System.err.println("do-first");
            })
            .next(c -> {
                System.err.println("do-second");
            })
            .choose(c -> {
                System.err.println("choose");
                return "left";
            }).ifSelected("left", c -> {
                System.err.println("  going left");
            }).ifSelected("right", c -> {
                System.err.println("  going right");
            })
            .end()
            .next(c -> {
                System.err.println("finally");
            });
        
        
        workflowService.execute(w, SimpleWorkflowContext.newContextFor(w));
    }
    
    @Test
    void testRightFirst() {
        Workflow<SimpleWorkflowContext> w = new Workflow<SimpleWorkflowContext>("test-workflow")
            .choose(c -> {
                System.err.println("choose");
                return "right";
            }).ifSelected("left", c -> {
                System.err.println("  going left");
            }).ifSelected("right", c -> {
                System.err.println("  going right");
            })
            .end()
            .next(c -> {
                System.err.println("finally");
            });
        
        
        workflowService.execute(w, SimpleWorkflowContext.newContextFor(w));
    }

}
