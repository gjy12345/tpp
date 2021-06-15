package cn.gjyniubi.cinema.common.aop;

import cn.gjyniubi.cinema.common.util.XssTool;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Parameter;

/**
 * @Author gujianyang
 * @Date 2021/6/7
 * @Class XssAop
 */
@Order(1)
@Slf4j
@Component
@Aspect
public class XssAop {

        @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void xssAop(){}

    @Around("xssAop()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        final Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = methodSignature.getMethod().getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if(parameters[i].getAnnotation(RequestBody.class)!=null){
                log.info("xss过滤");
                XssTool.encodeObject(args[i],parameters[i].getType());
                log.info("结果:{}",args[i]);
            }
        }
        return joinPoint.proceed(args);
    }
}
