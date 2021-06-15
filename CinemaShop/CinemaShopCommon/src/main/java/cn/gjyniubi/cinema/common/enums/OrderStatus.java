package cn.gjyniubi.cinema.common.enums;

/**
 * @Author gujianyang
 * @Date 2021/5/31
 * @Class OrderStatus
 */
public enum OrderStatus {
    WAIT_PAY(0),HAS_PAY(1),RETURN(-1),TIMEOUT(-2),USED(2);

    private final int code;

    public int getCode() {
        return code;
    }

    OrderStatus(int code) {
        this.code = code;
    }
}
