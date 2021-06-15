package cn.gjyniubi.cinema.common.exception;

import cn.gjyniubi.cinema.common.domain.JsonResult;

/**
 * @Author gujianyang
 * @Date 2021/3/3
 * @Class VerifyException
 */
public class VerifyException extends RuntimeException{

    private final JsonResult<?> result;

    public VerifyException(JsonResult<?> result) {
        super(result.getMsg());
        this.result=result;
    }

    public VerifyException(String message) {
        this(JsonResult.buildFail(message));
    }

    public JsonResult<?> getResult() {
        return result;
    }
}
