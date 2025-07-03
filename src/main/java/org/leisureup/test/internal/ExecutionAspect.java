package org.leisureup.test.internal;

import lombok.extern.slf4j.*;
import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.*;

@Slf4j
@Aspect
@Component
public class ExecutionAspect {

    private static String format(String signature, long start, long end) {
        return String.format("Execution time on [%30S]\t: %,d ms", signature, (end - start));
    }

    @Pointcut("@within(EstimateTime) || @annotation(EstimateTime)")
    public void estimateTimePointCut() {

    }

    @Around("estimateTimePointCut()")
    public Object estimateExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        String signature = joinPoint.getSignature().toShortString();

        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();
            log.info(format(signature, start, end));
        }
    }
}
