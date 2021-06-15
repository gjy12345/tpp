package cn.gjyniubi.cinema.common.util;

import cn.gjyniubi.cinema.common.exception.AssignmentException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author gujianyang
 * @Date 2021/5/18
 * @Class ArgsUtil
 */
public class ArgsUtil {

    private static final Map<String,List<Field>> fieldMap=new ConcurrentHashMap<>();

    public static <T> void trimAllStringValue(T data){
        if(data==null)
            return;
        List<Field> fields;
        if((fields=fieldMap.get(data.getClass().getName()))==null){
            fields=ClassTool.getObjectAllFields(data.getClass());
            fieldMap.put(data.getClass().getName(),fields);
        }
        for (Field field : fields) {
            if(field.getType()==String.class){
                try {
                    field.setAccessible(true);
                    String o = (String) field.get(data);
                    if(o!=null)
                        field.set(data,o.trim());
                }catch (Exception e){
                    throw new AssignmentException("转换首尾空格失败");
                }
            }
        }
    }

}
