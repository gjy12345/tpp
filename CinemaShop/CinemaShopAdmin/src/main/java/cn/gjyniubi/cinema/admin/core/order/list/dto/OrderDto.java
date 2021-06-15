package cn.gjyniubi.cinema.admin.core.order.list.dto;

import cn.gjyniubi.cinema.common.entry.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author gujianyang
 * @Date 2021/6/1
 * @Class OrderDto
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderDto extends Order {
    private String customerName;
    private String filmName;
}
