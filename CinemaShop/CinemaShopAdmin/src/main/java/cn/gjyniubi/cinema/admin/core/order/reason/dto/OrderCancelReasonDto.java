package cn.gjyniubi.cinema.admin.core.order.reason.dto;

import cn.gjyniubi.cinema.common.entry.OrderCancelReason;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author gujianyang
 * @Date 2021/5/13
 * @Class OrderCancelReasonDto
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderCancelReasonDto extends OrderCancelReason {
    private String createUser;
}
