package edu.pkch.mvcedu.commons.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* edu.pkch.mvcedu.api.*.controller.*.* (..))")
    private void controllerPointcut() {
    }

    @Around("controllerPointcut()")
    private Object requestAndResponseLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("request to {} with {}", joinPoint.getSignature(), joinPoint.getArgs());
        ResponseEntity<String> result = null;
        try {
            result = (ResponseEntity<String>) joinPoint.proceed();
            log.info("response code {}", result.getStatusCode());
            log.info("result {}", result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

}