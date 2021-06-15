package cn.gjyniubi.cinema.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/2/20
 * @Class ClassTool
 */
public class ClassTool {

    public static List<Field> getObjectAllFields(Class<?> c){
        List<Field> fields = new ArrayList<>(Arrays.asList(c.getDeclaredFields()));
        while (c.getSuperclass()!=null){
            c=c.getSuperclass();
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    //获取该类所有字段包括父类
    public static Field[] getClassAllFields(Class<?> c){
        List<Field> fields = getObjectAllFields(c);
        Field[] fs=new Field[fields.size()];
        for (int i = 0; i < fs.length; i++) {
            fs[i]=fields.get(i);
        }
        return fs;
    }

}
