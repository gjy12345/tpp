package cn.gjyniubi.cinema.common.util;

import cn.gjyniubi.cinema.common.exception.AssignmentException;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author gujianyang
 * @Date 2021/2/11
 * @Class BeanAssignment
 * json赋值给bean
 * 辅助类
 */
public class BeanAssignment {

    private static final Map<String, FieldInfo[]> classesFieldInfoMap=new ConcurrentHashMap<>();
    private static final Map<String,Map<String,Field>> classFieldMap=new ConcurrentHashMap<>();
//    /**
//     *
//     * @param json
//     * @param t
//     * @param <T>
//     * @return
//     */
//    public static <T> T assignmentObject(String json,T t){
//        if(t==null)
//            return null;
//        JSONObject jb=new JSONObject(json);
//        FieldInfo[] fields=getFieldInfos(t);
//        for (FieldInfo fieldInfo : fields) {
//            if(jb.has(fieldInfo.name)){
//                try {
//                    fieldInfo.field.set(t,getObjectFromJson(jb,fieldInfo.field.getType(),fieldInfo.name));
//                }catch (Exception e){
//                    throw new AssignmentException("assignmentObject失败:"+e.getMessage());
//                }
//            }
//        }
//        return t;
//    }

    public static <T> void copyValues(T from,T to){
        if(from==null||to==null)
            return;
        FieldInfo[] fields=getFieldInfos(to);
        Object v;
        for (FieldInfo field : fields) {
            try {
                v=field.field.get(from);
                if(v!=null){
                    field.field.set(to,v);
                }
            }catch (Exception e){
                throw new AssignmentException("copyValues失败:"+e.getMessage());
            }
        }
    }

    public static <T,R extends T> void copySuperValues(T from,R to){
        if(from==null||to==null)
            return;
        copySameFields(from,to);
    }

    public static <F,T> T copySameFields(F from,T to){
        if(from==null||to==null)
            return to;
        Map<String,Field> toFields=getFiledMap(to);
        Map<String,Field> fromFields=getFiledMap(from);
        toFields.keySet().parallelStream().forEach(s -> {
            Field field;
            if ((field=fromFields.get(s))!=null) {
                final Field toField = toFields.get(s);
                //static不管
                if (Modifier.isStatic(field.getModifiers())||Modifier.isStatic(toField.getModifiers())) {
                    return;
                }
                try {
                    toField.set(to,field.get(from));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        return to;
    }

//    private static Object getObjectFromJson(JSONObject jb,Class<?> type,String key){
//        if(type==String.class){
//            return jb.getString(key);
//        }else if(type==Integer.class||type==int.class){
//            return jb.getInt(key);
//        }else if(type==Long.class||type==long.class){
//            return jb.getBigInteger(key);
//        }else if(type==Double.class||type==double.class){
//            return jb.getDouble(key);
//        }else if(type==Boolean.class||type==boolean.class){
//            return jb.getBoolean(key);
//        }else if(type== BigDecimal.class){
//            return jb.getBigDecimal(key);
//        }
//        throw new RuntimeException("不支持的类型:"+type+" key:"+key);
//    }

    private static <T> FieldInfo[] getFieldInfos(T t){
        FieldInfo[] fields=classesFieldInfoMap.get(t.getClass().getName());
        if(fields==null){
            fields=getObjectAllFields(t.getClass());
            classesFieldInfoMap.put(t.getClass().getName(),fields);
        }
        return fields;
    }

    public static <T> Map<String,Field> getFiledMap(T t){
        Map<String,Field> fieldMap=classFieldMap.get(t.getClass().getName());
        if(fieldMap==null){
            fieldMap=new ConcurrentHashMap<>();
            FieldInfo[] fieldInfos=getFieldInfos(t);
            for (FieldInfo info : fieldInfos) {
                fieldMap.put(info.name,info.field);
            }
            classFieldMap.put(t.getClass().getName(),fieldMap);
        }
        return fieldMap;
    }

    //获取该类所有字段包括父类
    public static FieldInfo[] getObjectAllFields(Class<?> c){
        List<Field> fields = ClassTool.getObjectAllFields(c);
        FieldInfo[] fs=new FieldInfo[fields.size()];
        for (int i = 0; i < fs.length; i++) {
            fs[i]=new FieldInfo();
            fs[i].field=fields.get(i);
            fs[i].field.setAccessible(true);
            fs[i].name=fs[i].field.getName();
        }
        return fs;
    }

    @Data
    public static class FieldInfo{
        private Field field;
        private String name;
    }

}
