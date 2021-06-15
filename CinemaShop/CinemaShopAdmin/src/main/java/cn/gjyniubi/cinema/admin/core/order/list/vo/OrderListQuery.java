package cn.gjyniubi.cinema.admin.core.order.list.vo;

import cn.gjyniubi.cinema.common.domain.BaseTableQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/6/1
 * @Class OrderListQuery
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderListQuery extends BaseTableQuery {
    @Size(max = 20)
    private String phone;//这个手机号为用户的账号，不是下单填写的手机号
    private Date begin;
    private Date end;
    @Size(max = 20)
    private String filmName;
    @Max(2)
    @Min(-2)
    private Integer status;
}
