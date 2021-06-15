package cn.gjyniubi.cinema.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author gujianyang
 * @Date 2021/5/19
 * @Class TrimArgs
 */
@Target({ ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TrimArgs {

}
