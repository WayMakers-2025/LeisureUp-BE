package org.leisureup.global.logging.aspect;

import java.lang.reflect.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.*;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MethodLoggingAspect {

    private final MethodLoggingUtils utils;

    @Pointcut("""
            @within(org.leisureup.global.logging.LogMethodIO) ||
            @annotation(org.leisureup.global.logging.LogMethodIO)
            """)
    public void logMethodIO() {
    }

    @Pointcut("""
            @within(org.leisureup.global.logging.LogMethodInvocation) ||
            @annotation(org.leisureup.global.logging.LogMethodInvocation)
            """)
    public void logMethodInvocation() {
    }

    @Pointcut("""
            @within(org.leisureup.global.logging.LogMethodReturn) ||
            @annotation(org.leisureup.global.logging.LogMethodReturn)
            """)
    public void logMethodReturn() {
    }

    @Before("logMethodIO() || logMethodInvocation()")
    public void beforeMethodInvocation(JoinPoint joinPoint) {
        String targetName = joinPoint.getTarget().getClass().getSimpleName();

        Method method = utils.cast(joinPoint);
        String methodName = method.getName();
        String methodArgRepresentation = utils.representMethodArguments(
                method, joinPoint.getArgs()
        );

        log.info("{}#{} <== {}", targetName, methodName, methodArgRepresentation);
    }

    @AfterReturning(
            value = "logMethodIO() || logMethodReturn()",
            returning = "returnValue"
    )
    public void afterMethodReturn(JoinPoint joinPoint, Object returnValue) {
        String targetName = joinPoint.getTarget().getClass().getSimpleName();

        Method method = utils.cast(joinPoint);
        String methodName = method.getName();
        String methodReturnObjRepresentation = utils.representMethodReturnObj(
                method, returnValue
        );

        log.info("{}#{} ==> {}", targetName, methodName, methodReturnObjRepresentation);
    }
}
