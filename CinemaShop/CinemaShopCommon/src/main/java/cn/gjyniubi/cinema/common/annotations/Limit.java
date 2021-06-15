package cn.gjyniubi.cinema.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @Author gujianyang
 * @Date 2021/3/2
 * @Class Limit
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {
    String value();//字段
    long interval() default 15;//时间间隔
    TimeUnit unit() default TimeUnit.SECONDS;//单位
    int maxCount() default 3;//单位时间间隔内最多可以调用多少次
    String prefix();
    String msg() default "";
}
