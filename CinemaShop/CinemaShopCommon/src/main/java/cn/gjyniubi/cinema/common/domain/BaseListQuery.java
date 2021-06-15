package cn.gjyniubi.cinema.common.domain;

/**
 * @Author gujianyang
 * @Date 2021/5/25
 * @Class BaseListQuery
 */
public class BaseListQuery extends BaseSerializable{

    protected Integer offset;
    protected Integer pageSize;

    public Integer getOffset() {
        return offset==null?0:offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPageSize() {
        return Math.min(Math.abs((pageSize == null ? 10 : pageSize)), 50);
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
