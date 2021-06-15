package cn.gjyniubi.cinema.common.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;

/**
 * @Author gujianyang
 * @Date 2021/3/2
 * @Class AopUtil
 */
public class AopUtil {

    public static Object getMethodSignatureKey(JoinPoint point, String v) throws NoSuchFieldException, IllegalAccessException {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String[] value = v.trim().replace(" ", "").split("\\.");
        String[] parameterNames = methodSignature.getParameterNames();
        if (value.length == 0) {
            return null;
        }
        Object o = null;
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(value[0])) {
                o = point.getArgs()[i];
                break;
            }
        }
        if (value.length == 1 || o == null) {
            return o;
        }
        for (int i = 1; i < value.length; i++) {
            Field field = o.getClass().getDeclaredField(value[i]);
            field.setAccessible(true);
            o = field.get(o);
            if (o == null)
                return null;
        }
        return o;
    }
}
