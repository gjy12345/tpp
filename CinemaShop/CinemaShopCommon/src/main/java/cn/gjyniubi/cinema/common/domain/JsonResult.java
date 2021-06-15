package cn.gjyniubi.cinema.common.domain;

import cn.gjyniubi.cinema.common.enums.JsonResultType;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author gujianyang
 * @Date 2021/5/11
 * @Class JsonResult
 * 通用json返回值
 */
@ToString
public class JsonResult<T> implements Serializable {
    private static final long serialVersionUID=1;

    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> JsonResult<T> buildSuccess(T data){
        return buildSuccess(data,null);
    }

    public static <T> JsonResult<T> buildSuccess(T data,String message){
        return buildResult(JsonResultType.SUCCESS,data,message);
    }

    public static <T> JsonResult<T> buildWait(String message){
        return buildResult(JsonResultType.WAIT,null,message);
    }

    public static <T> JsonResult<T> buildFail(String message){
        return buildResult(JsonResultType.FAIL,null,message);
    }

    public static <T> JsonResult<T> buildFail(JsonResultType type,String message){
        return buildResult(type,null,message);
    }

    private static <T> JsonResult<T> buildResult(JsonResultType type,T data,String message){
        JsonResult<T> result=new JsonResult<>();
        result.setCode(type.getCode());
        result.setData(data);
        result.setMsg(message);
        return result;
    }
}
