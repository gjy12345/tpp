package cn.gjyniubi.cinema.common.aop;

import cn.gjyniubi.cinema.common.annotations.TrimArgs;
import cn.gjyniubi.cinema.common.util.ArgsUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;

/**
 * @Author gujianyang
 * @Date 2021/5/19
 * @Class TrimArgsAop
 */
@Order
@Slf4j
@Component
@Aspect
public class TrimArgsAop {

    @Pointcut("@annotation(cn.gjyniubi.cinema.common.annotations.TrimArgs)")
    public void trimArgsAop(){ }


    @Around("trimArgsAop()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        final Parameter[] parameters = signature.getMethod().getParameters();
        Object[] args = point.getArgs();
        log.info("清空参数首尾空格AOP");
        for (int i=0;i<parameters.length;i++) {
            if(parameters[i].getAnnotation(TrimArgs.class)!=null){
                ArgsUtil.trimAllStringValue(args[i]);
            }
        }
        return point.proceed(args);
    }

}
