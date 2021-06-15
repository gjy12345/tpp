package cn.gjyniubi.cinema.common.enums;

/**
 * @Author gujianyang
 * @Date 2021/5/31
 * @Class NumberType
 */
public enum  NumberType {
    ORDER("order");
    private String type;

    public String getType() {
        return type;
    }

    NumberType(String type) {
        this.type = type;
    }
}
