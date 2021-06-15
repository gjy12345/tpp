package cn.gjyniubi.cinema.common.enums;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class SystemUserType
 */

public enum  SystemUserType {

    //管理员，顾客，合作商
    ADMIN(0),CUSTOMER(1),PARTNER(2);

    private int value;

    public int getValue() {
        return value;
    }

    SystemUserType(int value) {
        this.value = value;
    }
}
