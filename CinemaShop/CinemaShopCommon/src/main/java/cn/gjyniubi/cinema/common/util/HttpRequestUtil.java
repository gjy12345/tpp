package cn.gjyniubi.cinema.common.util;

import cn.gjyniubi.cinema.common.entry.SysUser;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/3/2
 * @Class HttpRequestUtil
 * 获取登录用户
 */
public class HttpRequestUtil {

    private static final ThreadLocal<SysUser> userThreadLocal=new ThreadLocal<>();

    public static void setSysUser(SysUser sysUser){
        userThreadLocal.set(sysUser);
    }

    public static Integer getSysUserId(){
        return getSysUser().getId();
    }

    public static void removeSysUser(){
        userThreadLocal.remove();
    }

    public static Integer getUserType(){
        return userThreadLocal.get()==null?null:userThreadLocal.get().getUserType();
    }

    public static SysUser getSysUser(){
        return userThreadLocal.get();
    }

    //判断当前登录用户是否为合法身份
    public static boolean isAllowUser(List<Integer> allowUserType){
        if(allowUserType==null||allowUserType.isEmpty())
            return false;
        Integer userType=getUserType();
        for (Integer type : allowUserType) {
            if(userType.equals(type))
                return true;
        }
        return false;
    }
}
