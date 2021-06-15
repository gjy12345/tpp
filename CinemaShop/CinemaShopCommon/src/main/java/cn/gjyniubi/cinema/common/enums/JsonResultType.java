package cn.gjyniubi.cinema.common.enums;

/**
 * @Author gujianyang
 * @Date 2021/5/11
 * @Class JsonResultType
 */
public enum JsonResultType {

    SUCCESS(20000),FAIL(50000),NO_RULE(40003),NO_LOGIN(60204),NOT_FOUND(404),
    MISS_BIND_WECHAT(-98),WAIT(9876);

    private final int code;

    public int getCode(){
        return code;
    }

    JsonResultType(int code) {
        this.code = code;
    }

}
