package cn.gjyniubi.cinema.common.aop;

import cn.gjyniubi.cinema.common.annotations.CacheFirst;
import cn.gjyniubi.cinema.common.service.CommonRedisService;
import cn.gjyniubi.cinema.common.util.AopUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author gujianyang
 * @Date 2021/3/2
 * @Class CacheAop
 */
@Component
@Aspect
@Slf4j
public class CacheAop {

    @Autowired
    private CommonRedisService commonRedisService;

    @Pointcut("@annotation(cn.gjyniubi.cinema.common.annotations.CacheFirst)")
    public void cacheAop(){}

    @Around("cacheAop()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        CacheFirst first = ((MethodSignature) point.getSignature()).getMethod().getAnnotation(CacheFirst.class);
        Object key= first.noArgs()?"": AopUtil.getMethodSignatureKey(point,first.value());
        log.info("查找缓存:"+key);
        if(key==null&&!first.cacheOnArgsNull())
            return point.proceed();
        key=first.prefix()+key;
        log.info("查找缓存:"+key);
        Object o = commonRedisService.getValue(String.valueOf(key));
        if(o!=null){
            log.info("命中缓存:"+key);
            return o;
        }
        Object v=point.proceed();
        if(v!=null){
            if(first.cache())
                commonRedisService.setValue(key.toString(),v,first.cacheTime(),first.cacheTimeUnit());
        }
        return v;
    }



}
