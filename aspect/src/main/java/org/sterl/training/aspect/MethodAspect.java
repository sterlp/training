package org.sterl.training.aspect;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodAspect {
    
    /**
     * This ensures that the aspect is only woven into methods with the annotation.
     * Otherwise all methods are woven!
     */
    @Pointcut("execution(@MethodAnnotation * * (..))")
    private void isAnnotated() {};
    
    // https://www.eclipse.org/aspectj/doc/released/progguide/semantics-pointcuts.html
    // https://docs.spring.io/spring-framework/docs/5.3.x/reference/html/core.html#aop-pointcuts-designators
    
    /**
     * Run aspect with it is annotated and select directly the annotation.
     */
    @Around("isAnnotated() && @annotation(annotation)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, MethodAnnotation annotation) throws Throwable {
        final Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        long time = System.nanoTime();
        
        // access the annotation
        final String name = annotation.value().length() == 0 ?
                joinPoint.getSignature().getName() : annotation.value();
        
        try {
            return joinPoint.proceed();
        } finally {
            time = time - System.nanoTime();
            logger.info("Finished {} in {}ms", name, TimeUnit.NANOSECONDS.toMillis(time));
        }
    }
}
