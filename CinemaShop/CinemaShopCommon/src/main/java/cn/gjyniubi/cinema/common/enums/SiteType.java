package cn.gjyniubi.cinema.common.enums;

/**
 * @Author gujianyang
 * @Date 2021/5/23
 * @Class SiteType
 */
public enum  SiteType {
    //单人。维修。过道。情侣座左，情侣座右
    NORMAL(1),BAD(-1),AISLE(0),LOVER_LEFT(2),LOVER_RIGHT(3),UNKNOWN(-99);
    private int type;

    SiteType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static SiteType transferType(int type){
        switch (type){
            case 1:
                return NORMAL;
            case 2:
                return LOVER_LEFT;
            case 3:
                return LOVER_RIGHT;
            case 0:
                return AISLE;
            case -1:
                return BAD;
            default:
                return UNKNOWN;

        }
    }

    public static boolean isLegalSite(int type){
        switch (transferType(type)){
            case NORMAL:
            case LOVER_LEFT:
            case LOVER_RIGHT:
                return true;
        }
        return false;
    }
}
