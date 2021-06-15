package cn.gjyniubi.cinema.common.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gujianyang
 * @date 2020/12/4
 */
public class XssTool {
    private static final char[] unsafe={'\'','"','&','<','>'};
    private static final char[][] safe={"&#039;".toCharArray(),"&#34;".toCharArray(),"&#38;".toCharArray()
        ,"&#60;".toCharArray(),"&#62;".toCharArray()};

    //转换param到安全字符
    public static String encode(String str){
        if(str==null)
            return null;
        StringBuilder sb=new StringBuilder();
        int l=str.length();
        int index=0;
        char c;
        char[] arr=str.toCharArray();
        boolean replace;
        while (index<l){
            c=arr[index];
            replace=false;
            for (int i = 0; i < unsafe.length; i++) {
                if(c==unsafe[i]){
                    replace=true;
                    sb.append(safe[i]);
                    break;
                }
            }
            if(!replace){
                sb.append(c);
            }
            index++;
        }
        return sb.toString();
    }

    //转换回不安全编码
    private static String decode(String str){
        for (int i = 0; i < safe.length; i++) {
            str=str.replace(new String(safe[i]),unsafe[i]+"");
        }
        return str;
    }

    private static final Map<Class<?>, Field[]> fieldMap=new ConcurrentHashMap<>();

    public static void encodeObject(Object o,Class<?> c) throws Exception{
        if(List.class.isAssignableFrom(c)){
            List<?> list= (List<?>) o;
            for (Object value : list) {
                encodeObject(value);
            }
        }else if(Map.class.isAssignableFrom(c)){
            Map<?,?> map= (Map<?,?>) o;
            for (Object value : map.keySet()) {
                encodeObject(map.get(value));
            }
        }else if(c.getName().startsWith("cn.gjyniubi.cinema")){
            encodeObject(o);
        }else
            throw new RuntimeException("无法为"+c.getName()+"提供xss过过滤");
    }

    private static void encodeObject(Object o) throws Exception{
        if(o==null)
            return;
        Class<?> c=o.getClass();
        Field[] fields;
        if((fields=fieldMap.get(c))==null){
            fields=ClassTool.getClassAllFields(c);
            fieldMap.put(c,fields);
        }
        //替换不安全的字符
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(o) instanceof String) {
                field.set(o,XssTool.encode((String) field.get(o)));
            }
        }
    }
}
