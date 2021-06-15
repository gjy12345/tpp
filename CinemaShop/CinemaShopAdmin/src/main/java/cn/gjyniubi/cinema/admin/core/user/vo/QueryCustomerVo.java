package cn.gjyniubi.cinema.admin.core.user.vo;

import cn.gjyniubi.cinema.common.domain.BaseTableQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/5/24
 * @Class QueryCustomerVo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryCustomerVo extends BaseTableQuery {
    private String name;//模糊查询nickname和username
    private Integer sex;
    private Date begin;
    private Date end;
    private Integer locked;
    private String phone;
}
