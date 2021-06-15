package cn.gjyniubi.cinema.common.aop;

import cn.gjyniubi.cinema.common.annotations.Limit;
import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.exception.LimitException;
import cn.gjyniubi.cinema.common.service.CommonRedisService;
import cn.gjyniubi.cinema.common.util.AopUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author gujianyang
 * @Date 2021/3/2
 * @Class LimitAop
 */
@Component
@Aspect
@Slf4j
public class LimitAop {

    @Autowired
    private CommonRedisService commonRedisService;

    @Pointcut("@annotation(cn.gjyniubi.cinema.common.annotations.Limit)")
    public void limitAop(){}

    @Before("limitAop()")
    public void beforeDo(JoinPoint joinPoint) throws NoSuchFieldException, IllegalAccessException {
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Limit limit=methodSignature.getMethod().getAnnotation(Limit.class);
        Object key= AopUtil.getMethodSignatureKey(joinPoint,limit.value());
        if(key==null){
            //不处理，放行自行处理
            return;
        }
        String redisKey= UserContact.LIMIT_KEY_PREFIX+limit.prefix()+key;
        Integer value= commonRedisService.getValue(redisKey);
        value=value==null?1:value+1;
        if(value>limit.maxCount()){
            throw new LimitException(StringUtils.isBlank(limit.msg())?"操作频繁!":limit.msg());
        }
        Long hasTime=commonRedisService.getExpire(redisKey,limit.unit());
        if(hasTime==null||hasTime<=0){
            //没有设置超时时间或者key不存在
            hasTime=limit.interval();
        }
        commonRedisService.setValue(redisKey,value,hasTime,limit.unit());
    }
}
