package cn.gjyniubi.cinema.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @Author gujianyang
 * @Date 2021/3/2
 * @Class CacheFirst
 * 如果缓存，默认缓存30分钟
 * 默认不命中缓存时不缓存
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheFirst {
    String value();
    boolean cache() default false;
    int cacheTime() default 30;
    TimeUnit cacheTimeUnit() default TimeUnit.MINUTES;
    String prefix() default "";
    boolean noArgs() default false;
    boolean cacheOnArgsNull() default false;
}
