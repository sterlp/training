package org.sterl.training.common.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

    @Pointcut("execution(@LogMethod * * (..))")
    private void isAnnotated() {}
    @Pointcut("execution(* org.sterl.training..*(..))")
    private void isInPackage() {}

    @Around("isAnnotated() && isInPackage() && @annotation(annotation)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint,
                                   LogMethod annotation) throws Throwable {

        final Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String args = "";
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            args = Arrays.toString(joinPoint.getArgs());
        }
        long time = System.currentTimeMillis();
        try {
            var r = joinPoint.proceed();
            time = System.currentTimeMillis() - time;
            log.info("Called {}({}) in {}ms. Result={}", joinPoint.getSignature().getName(), args, time, r);
            return r;
        } catch (Exception e) {
            time = System.currentTimeMillis() - time;
            log.warn("Call failed {}({}) in {}ms. {}", joinPoint.getSignature().getName(), args, time, e.getMessage());
            throw e;
        }
    }

}
