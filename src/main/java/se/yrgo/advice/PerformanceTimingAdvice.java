package se.yrgo.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerformanceTimingAdvice {

    @Around("execution(* se.yrgo.services..*(..)) || execution(* se.yrgo.dataaccess..*(..))")
    public Object measurePerformance(ProceedingJoinPoint pjp) throws Throwable {

        long start = System.nanoTime();

        Object returnValue = pjp.proceed();

        long end = System.nanoTime();

        double timeMs = (end - start) / 1_000_000.0;

        System.out.println(
                "Time taken for the method "
                        + pjp.getSignature().getName()
                        + " from the class "
                        + pjp.getTarget().getClass().getName()
                        + " took "
                        + timeMs + "ms"
        );

        return returnValue;
    }
}