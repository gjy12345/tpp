package cn.gjyniubi.cinema.common.domain;

/**
 * @Author gujianyang
 * @Date 2021/5/13
 * @Class BaseTableQuery
 */
public class BaseTableQuery {

    protected Integer page;

    protected Integer pageSize;

    public Integer getPage() {
        return (page==null?0:page-1)*getPageSize();
    }

    public Integer realPage(){
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return Math.min(Math.abs((pageSize == null ? 10 : pageSize)), 50);
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
