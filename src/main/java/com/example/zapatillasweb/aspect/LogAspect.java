package com.example.zapatillasweb.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogAspect {

    // Define y monitorea todos los request para acceder al package controller
    @Pointcut("execution(* com.example.zapatillasweb.controller.*.*(..))")
    public void log(){}
    
    // Define el método a ejecutar antes de ingresar al cut plane
    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName()
            + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
        System.out.println("------------INICIANDO-----------------------");
        System.out.println("Request : " + requestLog);
    }
    // Define cómo completar la ejecución
    @After("log()")
    public void doAfter(){
        System.out.println("------------FINALIZADO-----------------------");
    }
    // Define el método para los valores devueltos por el Controller y loguea por consola
    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterReturning(Object result){
        System.out.println("Result : " + result);
    }
    // Calcular el tiempo de ejecución de cada request
    @Around("log()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + " ms");
        return result;
    }

    // Define una clase interna que representa la información relevante para loguear del request
    private class RequestLog {
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;
        public RequestLog(String url, String ip, String classMethod, Object[] args) {
        this.url = url;
        this.ip = ip;
        this.classMethod = classMethod;
        this.args = args;
        }
        @Override
        public String toString() {
        return "{" +
            "url='" + url + '\'' +
            ", ip='" + ip + '\'' +
            ", classMethod='" + classMethod + '\'' +
            ", args=" + Arrays.toString(args) +
            '}';
        }
    }
}

