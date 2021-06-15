package cn.gjyniubi.cinema.admin.core.order.reason.vo;

import cn.gjyniubi.cinema.common.domain.BaseTableQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author gujianyang
 * @Date 2021/5/13
 * @Class ReasonQueryVo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReasonQueryVo extends BaseTableQuery {
    private String reason;
    private Integer status;
}
