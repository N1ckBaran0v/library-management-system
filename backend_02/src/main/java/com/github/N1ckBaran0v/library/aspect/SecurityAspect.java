package com.github.N1ckBaran0v.library.aspect;

import com.github.N1ckBaran0v.library.service.ForbiddenException;
import com.github.N1ckBaran0v.library.service.UnauthorizedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Aspect
public class SecurityAspect {
    @Around("execution(* com.github.N1ckBaran0v.library.controller.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>("401 Unauthorized", HttpStatus.UNAUTHORIZED);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>("403 Forbidden", HttpStatus.FORBIDDEN);
        }
    }
}
